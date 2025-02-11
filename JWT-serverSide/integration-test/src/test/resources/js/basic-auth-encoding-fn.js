function (creds) {
  var credsPair = creds.username + ':' + creds.password;
  var Base64 = Java.type("java.util.Base64");
  var encoded = Base64.getEncoder().encodeToString(credsPair.bytes);
  return 'Basic ' + encoded;
}