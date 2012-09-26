/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.api.example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceList;
import com.google.api.services.compute.model.MachineType;
import com.google.api.services.compute.model.MachineTypeList;
import com.google.api.services.compute.model.Network;
import com.google.api.services.compute.model.NetworkInterface;
import com.google.api.services.compute.model.NetworkList;
import com.google.api.services.compute.model.Operation;
import com.google.api.services.compute.model.Zone;
import com.google.api.services.compute.model.ZoneList;
import com.google.common.base.Preconditions;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * An example for accessing the Google Compute API via Java.
 *
 * @author bburns@google.com (Brendan Burns)
 */
public class JCompute {
  /**
   * Browser to open in case {@link Desktop#isDesktopSupported()} is {@code false} or {@code null}
   * to prompt user to open the URL in their favorite browser.
   */
  private static final String BROWSER = "google-chrome";

  /**
   * The URI for the compute API.
   */
  private static final String COMPUTE_API =
      "https://www.googleapis.com/compute/v1beta12";

  /**
   * Loads the Google client secrets (if not already loaded).
   * See https://developers.google.com/accounts/docs/OAuth2#installed for more
   * details on authentication.
   *
   * @param jsonFactory JSON factory
   */
  private static GoogleClientSecrets loadClientSecrets(
      JsonFactory jsonFactory, String path) throws IOException {
    InputStream inputStream = new FileInputStream(path);
    Preconditions.checkNotNull(inputStream, "missing resource %s", path);
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
        inputStream);
    Preconditions.checkArgument(
        !clientSecrets.getDetails().getClientId().startsWith("[[")
        && !clientSecrets.getDetails().getClientSecret().startsWith("[["),
        "Please enter your client ID and secret from the Google APIs Console "
            + "in %s from the root samples directory", path);
    return clientSecrets;
  }


  /**
   * Authorizes the installed application to access user's protected data.
   *
   * @param transport HTTP transport
   * @param jsonFactory JSON factory
   * @param receiver verification code receiver
   * @param scopes OAuth 2.0 scopes
   */
  public static Credential authorize(
      HttpTransport transport, JsonFactory jsonFactory, String path,
      VerificationCodeReceiver receiver, Iterable<String> scopes) throws Exception {
    try {
      String redirectUri = receiver.getRedirectUri();
      GoogleClientSecrets clientSecrets = loadClientSecrets(jsonFactory, path);
      // redirect to an authorization page
      GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
          transport, jsonFactory, clientSecrets, scopes).build();
      browse(flow.newAuthorizationUrl().setRedirectUri(redirectUri).build());
      // receive authorization code and exchange it for an access token
      String code = receiver.waitForCode();
      GoogleTokenResponse response =
          flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
      // store credential and return it
      return flow.createAndStoreCredential(response, null);
    } finally {
      receiver.stop();
    }
  }

  /** Open a browser at the given URL. */
  private static void browse(String url) {
    // first try the Java Desktop
    if (Desktop.isDesktopSupported()) {
      Desktop desktop = Desktop.getDesktop();
      if (desktop.isSupported(Action.BROWSE)) {
        try {
          desktop.browse(URI.create(url));
          return;
        } catch (IOException e) {
          // handled below
        }
      }
    }
    // Next try rundll32 (only works on Windows)
    try {
      Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
      return;
    } catch (IOException e) {
      // handled below
    }
    // Next try the requested browser (e.g. "google-chrome")
    if (BROWSER != null) {
      try {
        Runtime.getRuntime().exec(new String[] {BROWSER, url});
        return;
      } catch (IOException e) {
        // handled below
      }
    }
    // Finally just ask user to open in their browser using copy-paste
    System.out.println("Please open the following URL in your browser:");
    System.out.println("  " + url);
  }

  /**
   * Print available machine types.
   * @param compute The main API access point
   * @param projectId The project ID.
   */
  public static void printMachineTypes(Compute compute, String projectId) {
    System.out.println("Machine Types:");
    try {
      Compute.MachineTypes.List types = compute.machineTypes().list(projectId);
      MachineTypeList list = types.execute();
      for (MachineType type : list.getItems()) {
        System.out.println(type.toPrettyString());
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Print available zones.
   * @param compute The main API access point
   * @param projectId The project ID.
   */
  public static void printZones(Compute compute, String projectId) {
    System.out.println("Zones:");
    try {
      Compute.Zones.List zones = compute.zones().list(projectId);
      ZoneList list = zones.execute();
      for (Zone zone : list.getItems()) {
        System.out.println(zone.toPrettyString());
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Print available machine instances.
   * @param compute The main API access point
   * @param projectId The project ID.
   */
  public static void printInstances(Compute compute, String projectId) {
    System.out.println("Instances:");
    try {
      Compute.Instances.List instances = compute.instances().list(projectId);
      InstanceList list = instances.execute();
      for (Instance instance : list.getItems()) {
        System.out.println(instance.toPrettyString());
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Print available networks.
   * @param compute The main API access point
   * @param projectId The project ID.
   */
  public static void printNetworks(Compute compute, String projectId) {
    try {
      Compute.Networks.List networks = compute.networks().list(projectId);
      NetworkList list = networks.execute();
      for (Network network : list.getItems()) {
        System.out.println(network.toPrettyString());
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Get input from System.in
   * @param prompt The string to print for the command prompt (e.g. '>')
   * @return The string entered by the user.
   */
  private static String getInput(String prompt) {
    System.out.print(prompt);
    return new Scanner(System.in).nextLine();
  }

  /**
   * Choose a GenericJson object from a list of objects. Given a list of
   * objects, displays the string named by 'descriptionField' for each object
   * and prompts the user to chose one.  Give a choice, returns the
   * 'resultField' for the chose object.
   * If choices.size() == 0, returns null
   * If choices.size() == 1, simply returns choices.at(0).get(resultField)
   * without prompting.
   *
   * @param choices The choices to choose from (returned from an API query)
   * @param resultField The JSON field name to use as the result.
   * @param descriptionField The JSON field name to use for display.  If null
   * use GenericJson.toPrettyString()
   * @return The 'resultField' value from the selected GenericJson object.
   */
  private static <T extends GenericJson> String chooseJsonObject(
      List<T> choices, String resultField, String descriptionField) {
    int ix = 1;
    if (choices.size() == 0) {
      return null;
    }
    if (choices.size() == 1) {
      return (String) choices.get(0).get(resultField);
    }
    for (GenericJson json : choices) {
      System.out.print((ix++) + ") ");
      if (descriptionField != null) {
        System.out.println(json.get(descriptionField));
      } else {
        System.out.println(json.toPrettyString());
      }
    }
    String select = getInput("Please select an option: ");
    GenericJson result = choices.get(Integer.parseInt(select) - 1);
    return (String) result.get(resultField);
  }

  /**
   * Starts an interactive shell for the Compute API.
   *
   * usage:
   * {@code java JCompute <project-id> </path/to/secrets.json>}
   *
   * project-id is the ID of your project on developers.google.com
   * secrets.json is a json file of your secret data for authentication,
   * for example:
   * {@code
     {
       "web": {
         "client_id": "foo"
         "client_secret": "bar"
         "redirect_uris": ["baz", "http://localhost"]
         "auth_uri": "https://accounts.google.com/o/oauth2/auth",
         "token_uri": "https://accounts.google.com/o/oauth2/token"
       }
     }
    }
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: java JCompute <project-id> <secrets.json>");
      System.exit(-1);
    }
    String projectId = args[0];
    String secretsPath = args[1];

    HttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    ArrayList<String> scopes = new ArrayList<String>();
    scopes.add("https://www.googleapis.com/auth/compute");

    VerificationCodeReceiver receiver = new PromptReceiver();
    Credential cred = JCompute.authorize(
        transport, jsonFactory, secretsPath, receiver, scopes);
    Compute compute = Compute.builder(transport, jsonFactory)
        .setApplicationName("jcompute")
        .setHttpRequestInitializer(cred)
        .build();
    String cmd = getInput("> ");
    for (; !cmd.startsWith("q"); cmd = getInput("> ")) {
      if (cmd.startsWith("q")) {
        break;
      } else if (cmd.equals("help")) {
        System.out.println(
            "Legal Commands: list_machine_types, list_zones, list_instances, "
                + "list_networks, create_instance, quit");
      } else if (cmd.equals("list_machine_types")) {
        printMachineTypes(compute, projectId);
      } else if (cmd.equals("list_zones")) {
        printZones(compute, projectId);
      } else if (cmd.equals("list_instances")) {
        printInstances(compute, projectId);
      } else if (cmd.equals("list_networks")) {
        printNetworks(compute, projectId);
      } else if (cmd.equals("create_instance")) {
        createInstance(projectId, jsonFactory, compute);
      } else {
        System.out.println("Unknown command: " + cmd);
      }
    }
  }


  /**
   * Create an instance and start it running.  Prompts the user for a bunch
   * of information related to creating the instance.
   *
   * @param projectId The project id under which the instance is created.
   * @param jsonFactory A JsonFactory for creating the API request.
   * @param compute The Compute API stub.
   * @throws IOException If a network error occurs.
   */
  private static void createInstance(String projectId, JsonFactory jsonFactory,
      Compute compute) throws IOException {
    Instance instance = new Instance();
    instance.setFactory(jsonFactory);

    // Select a machine type.
    String machine = chooseJsonObject(
        compute.machineTypes().list(projectId).execute().getItems(),
        "selfLink",
        "description");
    instance.setMachineType(machine);

    // Get a name from the user.
    String name = getInput("Please enter a name: ");
    instance.setName(name);

    // Use the default network.  Could select here if needed.
    List<NetworkInterface> networkInterfaces = new ArrayList<NetworkInterface>();
    NetworkInterface iface = new NetworkInterface();
    iface.setFactory(jsonFactory);
    iface.setName("eth0");
    iface.setNetwork(
        COMPUTE_API + "/projects/" + projectId + "/networks/default");
    networkInterfaces.add(iface);
    instance.setNetworkInterfaces(networkInterfaces);

    // Select a zone.
    String zone = chooseJsonObject(
        compute.zones().list(projectId).execute().getItems(),
        "selfLink",
        "name");
    instance.setZone(zone);
    Compute.Instances.Insert ins =
        compute.instances().insert(projectId, instance);

    // Finally, let's run it.
    Operation op = ins.execute();
    System.out.println(op.toPrettyString());
    System.out.println(instance.toPrettyString());
  }
}
