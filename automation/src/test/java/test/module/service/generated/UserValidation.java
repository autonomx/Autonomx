/** Auto generated code, do not modify. * */
package test.module.service.generated;

import org.testng.annotations.Test;

import core.support.objects.ServiceObject;
import serviceManager.ServiceRunner;
import test.module.service.TestBase;

public class UserValidation extends TestBase {
  /**
   * Retrieve a token from Token Generator
   *
   * @throws Exception
   */
  @Test(description = "Retrieve a token from Token Generator", priority = 1)
  public static void getAdminToken() throws Exception {
    ServiceObject service = new ServiceObject();
    service.withTestSuite("TsUser");
    service.withTestCaseID("getAdminToken");
    service.withDescription("Retrieve a token from Token Generator");
    service.withInterfaceType("RESTfulAPI");
    service.withUriPath("/admin/login");
    service.withContentType("application/json");
    service.withMethod("POST");
    service.withRequestBody(
        "{\"email\": \"<@adminUserName>\",\"password\": \"<@adminUserPassword>\"}");
    service.withOutputParams(
        ".user.roles..id:<$roles>; .token:<$accessTokenAdmin>;.user.id:<$userId>");
    service.withRespCodeExp("200");
    service.withExpectedResponse(
        "{ \"data\": { \"user\": { \"id\": 1, \"firstname\": \"auto \", \"lastname\": \"user\", \"username\": \"autoAdmin1\", \"email\": \"autouser313@gmail.com\", \"registrationToken\": null, \"isActive\": true, \"blocked\": null, } } }");
    service.withTcIndex("0:6");
    service.withTcName("UserValidation");
    service.withTcType("service");
    ServiceRunner.TestRunner(service);
  }
  /**
   * create user
   *
   * @throws Exception
   */
  @Test(description = "create user", priority = 2)
  public static void createUser() throws Exception {
    ServiceObject service = new ServiceObject();
    service.withTestSuite("TsUser");
    service.withTestCaseID("createUser");
    service.withDescription("create user");
    service.withInterfaceType("RESTfulAPI");
    service.withUriPath("/content-manager/collection-types/plugins::users-permissions.user");
    service.withContentType("application/json");
    service.withMethod("POST");
    service.withOption("DEPENDS_ON_TEST:getAdminToken");
    service.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>");
    service.withRequestBody(
        "{\"confirmed\":false,\"blocked\":false,\"username\":\"zzz_test<@_RAND16>\",\"email\":\"testuser+<@_TIME_MS_24>@gmail.com\",\"password\":\"password<@_TIME_MS_24>\"}");
    service.withOutputParams("id:<$userId>");
    service.withRespCodeExp("201");
    service.withExpectedResponse(
        "_VERIFY.JSON.PART_ username:1:equalTo(zzz_test<@_RAND16>); email:1:equalTo(testuser+<@_TIME_MS_24>@gmail.com);");
    service.withTcIndex("1:6");
    service.withTcName("UserValidation");
    service.withTcType("service");
    ServiceRunner.TestRunner(service);
  }
  /**
   * create user no token
   *
   * @throws Exception
   */
  @Test(description = "create user no token", priority = 3)
  public static void createUserNoToken() throws Exception {
    ServiceObject service = new ServiceObject();
    service.withTestSuite("TsUser");
    service.withTestCaseID("createUserNoToken");
    service.withDescription("create user no token");
    service.withInterfaceType("RESTfulAPI");
    service.withUriPath("/content-manager/collection-types/plugins::users-permissions.user");
    service.withContentType("application/json");
    service.withMethod("POST");
    service.withRequestHeaders("NO_TOKEN");
    service.withRequestBody(
        "{\"confirmed\":false,\"blocked\":false,\"username\":\"zzz_test<@_RAND16>\",\"email\":\"testuser+<@_TIME_MS_24>@gmail.com\",\"password\":\"password<@_TIME_MS_25>\"}");
    service.withRespCodeExp("403");
    service.withExpectedResponse(
        "{\"statusCode\":403,\"error\":\"Forbidden\",\"message\":\"Forbidden\"}");
    service.withTcIndex("2:6");
    service.withTcName("UserValidation");
    service.withTcType("service");
    ServiceRunner.TestRunner(service);
  }
  /**
   * create user invalid token
   *
   * @throws Exception
   */
  @Test(description = "create user invalid token", priority = 4)
  public static void createUserInvalidToken() throws Exception {
    ServiceObject service = new ServiceObject();
    service.withTestSuite("TsUser");
    service.withTestCaseID("createUserInvalidToken");
    service.withDescription("create user invalid token");
    service.withInterfaceType("RESTfulAPI");
    service.withUriPath("/content-manager/collection-types/plugins::users-permissions.user");
    service.withContentType("application/json");
    service.withMethod("POST");
    service.withOption("WAIT_FOR_RESPONSE:60");
    service.withRequestHeaders("INVALID_TOKEN");
    service.withRequestBody(
        "{\"confirmed\":false,\"blocked\":false,\"username\":\"zzz_test<@_RAND16>\",\"email\":\"testuser+<@_TIME_MS_24>@gmail.com\",\"password\":\"password<@_TIME_MS_26>\"}");
    service.withRespCodeExp("401");
    service.withExpectedResponse("{\"statusCode\":401,\"error\":\"Unauthorized\"}");
    service.withTcIndex("3:6");
    service.withTcName("UserValidation");
    service.withTcType("service");
    ServiceRunner.TestRunner(service);
  }
  /**
   * update user
   *
   * @throws Exception
   */
  @Test(description = "update user", priority = 5)
  public static void updateUser() throws Exception {
    ServiceObject service = new ServiceObject();
    service.withTestSuite("TsUser");
    service.withTestCaseID("updateUser");
    service.withDescription("update user");
    service.withInterfaceType("RESTfulAPI");
    service.withUriPath(
        "/content-manager/collection-types/plugins::users-permissions.user/<@userId>");
    service.withContentType("application/json");
    service.withMethod("PUT");
    service.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>");
    service.withRequestBody(
        "{\"username\":\"zzz_update<@_RAND16>\",\"email\":\"testupdate+<@_TIME_MS_24>@gmail.com\",\"password\":\"password<@_TIME_MS_24>\",\"confirmed\":true}");
    service.withRespCodeExp("200");
    service.withExpectedResponse(
        "_VERIFY.JSON.PART_ username:1:equalTo(zzz_update<@_RAND16>); email:1:equalTo(testupdate+<@_TIME_MS_24>@gmail.com);");
    service.withTcIndex("4:6");
    service.withTcName("UserValidation");
    service.withTcType("service");
    ServiceRunner.TestRunner(service);
  }
  /**
   * delete user
   *
   * @throws Exception
   */
  @Test(description = "delete user", priority = 6)
  public static void deleteUser() throws Exception {
    ServiceObject service = new ServiceObject();
    service.withTestSuite("TsUser");
    service.withTestCaseID("deleteUser");
    service.withDescription("delete user");
    service.withInterfaceType("RESTfulAPI");
    service.withUriPath(
        "/content-manager/collection-types/plugins::users-permissions.user/<@userId>");
    service.withMethod("DELETE");
    service.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>");
    service.withRespCodeExp("200");
    service.withTcIndex("5:6");
    service.withTcName("UserValidation");
    service.withTcType("service");
    ServiceRunner.TestRunner(service);
  }
}
