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

# Google Compute Engine JavaScript Sample Application

== Description
This is a simple web-based example of calling the Google Compute Engine API
in JavaScript.

## Setup Authentication
  1) Visit https://code.google.com/apis/console/?api=compute to register your
  application.
    - From the "Project Home" screen, activate access to "Google Compute Engine
    API".
    - Click on "API Access" in the left column
    - Click the button labeled "Create an OAuth2 client ID"
    - Give your application a name and click "Next"
    - Select "Web Application" as the "Application type"
    - Click "Create client ID"
    - Click "Edit..." for your new client ID
    - Under the callback URL, enter the location of your JavaScript application.

  2) Update app.html with the redirect uri, consumer key, secret, and
  developer key obtained in step 1.
    - Update 'YOUR_CLIENT_ID' with your oauth2 client id.
    - Update 'YOUR_API_KEY' with your developer key.
    This is listed under "Simple API Access" at the very bottom in the Google
    API Console.

## Running the Sample Application
  3) Load app.html on your web server, and visit the appropriate website in
  your web browser.
