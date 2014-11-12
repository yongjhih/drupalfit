drupalfit
=========

[![Build Status](https://travis-ci.org/yongjhih/drupalfit.png?branch=master)](https://travis-ci.org/yongjhih/drupalfit) [![Stories in Ready](https://badge.waffle.io/yongjhih/drupalfit.png)](http://waffle.io/yongjhih/drupalfit) 
[![Bountysource](https://www.bountysource.com/badge/team?team_id=43965&style=bounties_posted)](https://www.bountysource.com/teams/8tory/bounties?utm_source=8tory&utm_medium=shield&utm_campaign=bounties_posted)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.infstory/drupalfit/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.infstory/drupalfit)

![square drupal](drupal-webtreatsetc.png "Square drupal")

A drupal services rest client with retrofit for android

![Screenshot](app/screenshot.png "Sign-up sample")

Usage
=====

* Initialization

```java
DrupalManager.get().setEndpoint("https://example.com/api");
```

* register

```java
DrupalManager.get().register("foo", "foo@example.com", "password", new Callback<User>() {
    @Override
    public void success(User user, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

* login

```java
DrupalManager.get().login("foo", "password", new Callback<Login>() {
    @Override
    public void success(Login login, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

* logout (after userLogin)

```java
DrupalManager.get().logout(new Callback<Logout>() {
    @Override
    public void success(Logout logout, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

* getNode

```java
DrupalManager.get().getNode(1, new Callback<Node>() {
    @Override
    public void success(Node node, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

Bonus
=====

* getProfile with facebook access token (Depend on [yongjhih/drupal-hybridauth](https://github.com/yongjhih/drupal-hybridauth))

```java
DrupalManager.get().setProvider(this, DrupalManager.FACEBOOK, "faceb00k_t0ken");

DrupalManager.get().getProfile(new Callback<User>() {
    @Override
    public void success(User user, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

Installation
============

build.gradle:

```gradle
dependencies {
    compile "com.infstory:drupalfit:+"
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
