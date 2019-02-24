import fcntl
import logging
import os
import pty
import subprocess
import sys
import termios
import time

from execution import process_base
from utils import process_utils

script_encodings = {}

LOGGER = logging.getLogger('script_server.process_pty')


def _unset_output_flags(fd, *new_attributes):
    attributes = termios.tcgetattr(fd)

    for attribute in new_attributes:
        # 1 stays for oflag (output flags)
        attributes[1] = attributes[1] & ~attribute

    termios.tcsetattr(fd, termios.TCSANOW, attributes)


class PtyProcessWrapper(process_base.ProcessWrapper):
    def __init__(self, command, working_directory):
        super().__init__(command, working_directory)

        self.pty_master = None
        self.pty_slave = None

        self.encoding = get_encoding(command, working_directory)

    def start_execution(self, command, working_directory):
        master, slave = pty.openpty()
        self.process = subprocess.Popen(command,
                                        cwd=working_directory,
                                        stdin=slave,
                                        stdout=slave,
                                        stderr=slave,
                                        start_new_session=True)
        self.pty_slave = slave
        self.pty_master = master

        # ONLCR - transform \n to \r\n
        _unset_output_flags(self.pty_master, termios.ONLCR)
        fcntl.fcntl(self.pty_master, fcntl.F_SETFL, os.O_NONBLOCK)

    def write_to_input(self, value):
        input_value = value
        if not input_value.endswith("\n"):
            input_value += "\n"

        os.write(self.pty_master, input_value.encode())

    def wait_finish(self):
        self.process.wait()

    def pipe_process_output(self):
        try:
            while True:
                finished = False
                wait_new_output = False

                max_read_bytes = 1024

                if self.is_finished():
                    data = b""
                    while True:
                        try:
                            chunk = os.read(self.pty_master, max_read_bytes)
                            data += chunk

                        except BlockingIOError:
                            break

                    finished = True

                else:
                    data = ""
                    try:
                        data = os.read(self.pty_master, max_read_bytes)
                        if data.endswith(b"\r"):
                            data += os.read(self.pty_master, 1)

                        if data and (self.encoding.lower() == "utf-8"):

                            while data[len(data) - 1] >= 127:
                                next_byte = os.read(self.pty_master, 1)
                                if not next_byte:
                                    break

                                data += next_byte
                    except BlockingIOError:
                        if self.is_finished():
                            finished = True

                    if not data:
                        wait_new_output = True

                if data:
                    output_text = data.decode(self.encoding)
                    self._write_script_output(output_text)

                if finished:
                    break

                if wait_new_output:
                    time.sleep(0.01)

        except:
            self._write_script_output("Unexpected error occurred. Contact the administrator.")

            try:
                self.kill()
            except:
                LOGGER.exception('Failed to kill a process')

            LOGGER.exception('Failed to read script output')

        finally:
            os.close(self.pty_master)
            os.close(self.pty_slave)
            self.output_stream.close()


def get_encoding(command, working_directory):
    encoding = None

    split_command = command
    if isinstance(command, str):
        split_command = process_utils.split_command(command, working_directory)

    if split_command and split_command[0]:
        program = split_command[0]
        if program in script_encodings:
            encoding = script_encodings[program]

    if not encoding:
        if sys.stdout.encoding:
            encoding = sys.stdout.encoding
        else:
            encoding = 'utf-8'

    return encoding
