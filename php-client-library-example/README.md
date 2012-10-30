# Copyright 2012 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Google Compute Engine PHP Sample Application

== Description
This is a simple web-based example of calling the Google Compute Engine API
in PHP.

## Prerequisites:
Please make sure that all of the following is installed before trying to run
the sample application.

- PHP 5.2.x or higher [http://www.php.net/]
- PHP Curl extension [http://www.php.net/manual/en/intro.curl.php]
- PHP JSON extension [http://php.net/manual/en/book.json.php]
- The google-api-php-client library checked out locally

## Setup Authentication
  NOTE: This README assumes that you have enabled access to the Google Compute
  Engine API via the Google API Console page.

  1) Visit https://code.google.com/apis/console/?api=compute to register your
  application.
    - Click on "API Access" in the left column
    - Click the button labeled "Create an OAuth2 client ID..." if you have not
      generated any client IDs, or "Create another client ID..." if you have
    - Give your application a name and click "Next"
    - Select "Web Application" as the "Application type"
    - Click "Create client ID"
    - Click "Edit settings..." for your new client ID
    - Under the redirect URI, enter the location of your JavaScript application
    - Click "Update"

  2) Update app.php with the redirect uri, consumer key, secret, and developer
  key obtained in step 1.
    - Update 'YOUR_CLIENT_ID' with your oauth2 client id.
    - Update 'YOUR_CLIENT_SECRET' with your oauth2 client secret.
    - Update 'YOUR_REDIRECT_URI' with the fully qualified
    redirect URI.
    - Update 'YOUR_DEVELOPER_KEY' with your developer key.
    This is listed under "Simple API Access" at the very bottom of the page,
    in the Google API Console.

## Running the Sample Application
  3) Load app.php on your web server, and visit the appropriate website in
  your web browser.
