/** Auto generated code, do not modify. * */
package test.module.service.generated;

import core.support.objects.ServiceObject;
import serviceManager.ServiceRunner;
import org.testng.annotations.Test;
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
    service.withUriPath("/auth/local");
    service.withContentType("application/json");
    service.withMethod("POST");
    service.withRequestBody(
        "{ \"identifier\": \"<@adminUserName>\", \"password\": \"<@adminUserPassword>\" }");
    service.withOutputParams("user.role.id:<$roles>; jwt:<$accessTokenAdmin>; user.id:<$userId>");
    service.withRespCodeExp("200");
    service.withExpectedResponse(
        "{ \"user\": { \"username\": \"<@adminUserName>\", \"email\": \"autouser313@gmail.com\", \"provider\": \"local\", \"confirmed\": true, \"blocked\": null } } && _VERIFY.JSON.PART_ user:jsonbody([{ \"username\": \"<@adminUserName>\", \"email\": \"autouser313@gmail.com\", \"provider\": \"local\", \"confirmed\": true, \"blocked\": null }]); user[?(@.username == \"autoAdmin1\")].provider:equalTo(local); user.username:1: hasItems(<@adminUserName>); user.email:1: equalTo(autouser313@gmail.com); user.provider:1: isNotEmpty; user.role.name:1: contains(Administrator) && _NOT_EMPTY_");
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
    service.withUriPath("/content-manager/explorer/user/?source=users-permissions");
    service.withContentType("application/x-www-form-urlencoded");
    service.withMethod("POST");
    service.withOption("DEPENDS_ON_TEST:getAdminToken");
    service.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>");
    service.withRequestBody(
        "username:zzz_test<@_RAND16><@_INCREMENT_FROM_1>, email:testuser+<@_TIME_MS_24><@_INCREMENT_FROM_1>@gmail.com, password:password<@_TIME_MS_24>, confirmed:true");
    service.withOutputParams("id:<$userId>");
    service.withRespCodeExp("201");
    service.withExpectedResponse(
        "{ \"provider\": \"local\", \"blocked\": null } && _VERIFY.JSON.PART_ id: isNotEmpty");
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
    service.withUriPath("/content-manager/explorer/user/?source=users-permissions");
    service.withContentType("application/x-www-form-urlencoded");
    service.withMethod("POST");
    service.withRequestHeaders("NO_TOKEN");
    service.withRequestBody(
        "username:zzz_test<@_TIME_MS_24>, email:testuser+<@_TIME_MS_24>@gmail.com, password:password<@_TIME_MS_24>, confirmed:true");
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
    service.withUriPath("/content-manager/explorer/user/?source=users-permissions");
    service.withContentType("application/x-www-form-urlencoded");
    service.withMethod("POST");
    service.withOption("WAIT_FOR_RESPONSE:60");
    service.withRequestHeaders("INVALID_TOKEN");
    service.withRequestBody(
        "username:zzz_test<@_RAND16>, email:testuser+<@_TIME_MS_24>@gmail.com, password:password<@_TIME_MS_24>, confirmed:true");
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
    service.withUriPath("/content-manager/explorer/user/<@userId>?source=users-permissions");
    service.withContentType("application/json");
    service.withMethod("PUT");
    service.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>");
    service.withRequestBody(
        "{\"username\":\"zzz_update<@_RAND16>\", \"email\":\"testUpdate+<@_TIME_MS_24>@gmail.com\", \"password\":\"password<@_TIME_MS_24>\", \"confirmed\":true}");
    service.withRespCodeExp("200");
    service.withExpectedResponse(
        "{\"id\":<@userId>, \"username\":\"zzz_update<@_RAND16>\", \"email\":\"testUpdate+<@_TIME_MS_24>@gmail.com\", \"provider\":\"local\", \"confirmed\":true, \"blocked\":null, \"role\": { \"name\":\"Authenticated\", \"description\":\"Default role given to authenticated user.\", \"type\":\"authenticated\" } } && _VERIFY.JSON.PART_ role:jsonbody([{ \"name\":\"Authenticated\", \"description\":\"Default role given to authenticated user.\", \"type\":\"authenticated\" }])");
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
    service.withUriPath("/content-manager/explorer/user/<@userId>?source=users-permissions");
    service.withMethod("DELETE");
    service.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>");
    service.withRespCodeExp("200");
    service.withTcIndex("5:6");
    service.withTcName("UserValidation");
    service.withTcType("service");
    ServiceRunner.TestRunner(service);
  }
}
