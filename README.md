drupalfit
=========

A drupal services rest client with retrofit for android

![Screenshot](app/screenshot.png "Sign-up sample")

Usage
=====

* userRegister (https://example.com/api/user/register)

Ref: [HomeActivity.java#L58](app/src/main/java/drupalfit/sample/HomeActivity.java#L58)

```java
DrupalManager.get().getService("https://example.com/api").userRegister("foo", "foo@example.com", "password", new Callback<User>() {
    @Override
    public void success(User user, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

* userLogin (https://example.com/api/user/login)

```java
DrupalManager.get().getService("https://example.com/api").userLogin("foo", "password", new Callback<Login>() {
    @Override
    public void success(Login login, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```
