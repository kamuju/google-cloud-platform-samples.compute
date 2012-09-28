# Google Compute Engine PHP Sample Application

== Description
This is a simple web-based example of calling Google Compute Engine APIs in
PHP.

## Prerequisites:
Please make sure that all of the following is installed before trying to run
the sample application.

- PHP 5.2.x or higher [http://www.php.net/]
- PHP Curl extension [http://www.php.net/manual/en/intro.curl.php]
- PHP JSON extension [http://php.net/manual/en/book.json.php]
- The google-api-php-client library checked out locally

## Setup Authentication
  1) Visit https://code.google.com/apis/console/?api=compute to register
  your application.
    - From the "Project Home" screen, activate access to "Google Compute
    Engine API".
    - Click on "API Access" in the left column
    - Click the button labeled "Create an OAuth2 client ID"
    - Give your application a name and click "Next"
    - Select "Web Application" as the "Application type"
    - Click "Create client ID"
    - Click "Edit..." for your new client ID
    - Under the callback URL, enter the location of your PHP application.

  2) Update app.php with the redirect uri, consumer key, secret, and developer
  key obtained in step 1.
    - Update 'insert_your_oauth2_client_id' with your oauth2 client id.
    - Update 'insert_your_oauth2_client_secret' with your oauth2 client secret.
    - Update 'insert_your_oauth2_redirect_uri' with the fully qualified
    redirect URI.
    - Update 'insert_your_developer_key' with your developer key.
    This is listed under "Simple API Access" at the very bottom in the Google
    API Console.

## Running the Sample Application
  3) Load app.php on your web server, and visit the appropriate website in
  your web browser.
