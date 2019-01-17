import sys
import time
from datetime import datetime, timezone

MS_IN_SEC = 1000
MS_IN_MIN = 60 * MS_IN_SEC
MS_IN_HOUR = 60 * MS_IN_MIN
MS_IN_DAY = 24 * MS_IN_HOUR


def get_current_millis():
    return int(round(time.time() * 1000))


def to_millis(datetime):
    return int(round(datetime.timestamp() * 1000))


def ms_to_datetime(time_millis):
    return datetime.fromtimestamp(time_millis / 1000.0, tz=timezone.utc)


def sec_to_datetime(time_seconds):
    return datetime.fromtimestamp(time_seconds, tz=timezone.utc)


def datetime_now():
    return datetime.now(tz=timezone.utc)


def astimezone(datetime_value, new_timezone):
    if (datetime_value.tzinfo is not None) or (sys.version_info >= (3, 6)):
        return datetime_value.astimezone(new_timezone)
    else:
        # From documentation: Naive datetime instances are assumed to represent local time
        timestamp = datetime_value.timestamp()
        local_tz_offset = datetime.fromtimestamp(timestamp) - datetime.utcfromtimestamp(timestamp)
        local_timezone = timezone(local_tz_offset)
        datetime_with_local_tz = datetime_value.replace(tzinfo=local_timezone)
        transformed = datetime_with_local_tz.astimezone(new_timezone)
        return transformed


def days_to_ms(days):
    return days * MS_IN_DAY


def ms_to_days(ms):
    return float(ms) / MS_IN_DAY
