drupalfit
=========

![square drupal](drupal-webtreatsetc.png "Square drupal")

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

Installation
============

# build.gradle

```gradle
dependencies {
    ...
    compile "com.infstory:drupalfit:+"
}

repositories {
    ...
    maven { url "https://oss.sonatype.org/content/repositories/releases/" }
}
```

[License] (LICENSE)
===================

```
The MIT License (MIT)

Copyright (c) 2014 Andrew Chen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
