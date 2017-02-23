/**
 * Created by stephan on 20.03.16.
 */

$(function () {
    // VARIABLES =============================================================
    var TOKEN_KEY = "jwtToken"
    var $notLoggedIn = $("#notLoggedIn");
    var $loginForm = $("#loginForm");
    var $loggedIn = $("#loggedIn").hide();
    var $response = $("#response");
    var $login = $("#login");
    var $signUp = $("#signUp");
    var $userInfo = $("#userInfo").hide();

    // FUNCTIONS =============================================================
    function getJwtToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    function setJwtToken(token) {
        localStorage.setItem(TOKEN_KEY, token);
    }

    function removeJwtToken() {
        localStorage.removeItem(TOKEN_KEY);
    }

   function doLogin(loginData) {
          $.ajax({
              url: "/auth",
              type: "POST",
              data: JSON.stringify(loginData),
              contentType: "application/json; charset=utf-8",
              dataType: "json",
              success: function (data, textStatus, jqXHR) {
                  setJwtToken(data.token);
                  $login.hide();
                  $notLoggedIn.hide();
                  showTokenInformation()
                  showUserInformation();
              },
              error: function (jqXHR, textStatus, errorThrown) {
                  if (jqXHR.status === 401) {
                      $('#loginErrorModal')
                          .modal("show")
                          .find(".modal-body")
                          .empty()
                          .html("<p>" + errorThrown + "</p>");
                  } else {
                      throw new Error( errorThrown);
                  }
              }
          });
      }

    $('loginErrorModal').modal('show', '.modal', function () {
      $(this).removeData('bs.modal');
    });

    function doLogout() {
        removeJwtToken();
        $login.show();
        $loginForm.show();
        $userInfo
            .hide()
            .find("#userInfoBody").empty();
        $loggedIn
            .hide()
            .attr("title", "")
            .empty();
        $notLoggedIn.show();
        $signUp.show();
    }

    function createAuthorizationTokenHeader() {
        var token = getJwtToken();
        if (token) {
            return {"Authorization": token};
        } else {
            return {};
        }
    }

     function validateCurrentToken() {
             $.ajax({
                 url: "/api/auth/refresh",
                 type: "GET",
                 contentType: "application/json; charset=utf-8",
                 dataType: "json",
                 headers: createAuthorizationTokenHeader(),
                  success: function(data, textStatus, xhr) {

                     },
                     complete: function(xhr, textStatus) {
                         console.log(xhr.status);
                         if(xhr.status==401)
                            doLogout();
                     },
                      error: function(xhr, status, error){
                      },
             });
         }

    function showUserInformation() {
        $.ajax({
            url: "/api/user",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                var $userInfoBody = $userInfo.find("#userInfoBody");

                $userInfoBody.append($("<div>").text("Username: " + data.username));
                $userInfoBody.append($("<div>").text("Email: " + data.email));

                var $authorityList = $("<ul>");
                data.authorities.forEach(function (authorityItem) {
                    $authorityList.append($("<li>").text(authorityItem.authority));
                });
                var $authorities = $("<div>").text("Authorities:");
                $authorities.append($authorityList);

                $userInfoBody.append($authorities);
                $userInfo.show();
            }
        });
    }

    function showTokenInformation() {
        $loggedIn
            .text("Token: " + getJwtToken())
            .attr("title", "Token: " + getJwtToken())
            .show();
    }

    function showResponse(statusCode, message) {
        $response
            .empty()
            .text("status code: " + statusCode + "\n-------------------------\n" + message);
    }

    // REGISTER EVENT LISTENERS =============================================================
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        var $form = $(this);
        var formData = {
            username: $form.find('input[name="username"]').val(),
            password: $form.find('input[name="password"]').val()
        };

        doLogin(formData);
    });

    $("#logoutButton").click(doLogout);

    $("#exampleServiceBtn").click(function () {
        $.ajax({
            url: "/persons",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                showResponse(jqXHR.status, JSON.stringify(data));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponse(jqXHR.status, errorThrown);
            }
        });
    });

    $("#adminServiceBtn").click(function () {
        $.ajax({
            url: "/admin",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                showResponse(jqXHR.status, data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponse(jqXHR.status, errorThrown);
            }
        });
    });

    $loggedIn.click(function () {
        $loggedIn
                .toggleClass("text-hidden")
                .toggleClass("text-shown");
    });

    // INITIAL CALLS =============================================================

    validateCurrentToken()

     if (getJwtToken()) {
        $login.hide();
        $loginForm.hide();
        $signUp.hide();
        $notLoggedIn.hide();
        showTokenInformation();
        showUserInformation();
    }

 });