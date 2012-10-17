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

# Google Compute Engine Ruby Sample Application

== Description
This is a simple command line example of calling the Google Compute Engine
APIs in Ruby.

## Prerequisites
Please make sure that all of the following is installed before trying to run
the sample application.

- Ruby 1.9.3+
- The following gems (run 'sudo gem install <gem name>' to install)
    - google-api-client
    - thin
    - launchy
- The google-api-ruby-client library checked out locally, and this sample
  application running from inside of that repo.

## Setup Authentication
  1) Visit https://code.google.com/apis/console/ to register your application.
    - From the "Project Home" screen, activate access to "Google Compute Engine
    API".
    - Click on "API Access" in the left column
    - Click the button labeled "Create an OAuth 2.0 client ID"
    - Give your application a name and click "Next"
    - Select "Installed Application" as the "Application type"
    - Select "Other" under "Installed application type"
    - Click "Create client ID"

Edit the client_secrets.json file and enter the client ID and secret that you
retrieved from the API Console.

## Running the Sample Application
1. Run the application
    $ ruby sample.rb
2. Authorize the application in the browser window that opens.
3. The Google Compute Engine sample application will display its output on the
command line.
