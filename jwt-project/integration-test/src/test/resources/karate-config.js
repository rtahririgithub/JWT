function() {
	
  karate.log('Env Params :', karate.env);
  
  var envParamsStr = karate.env;
  var envParams = envParamsStr.split(',');
  
  var appName = envParams[0];
  var envName = envParams[1];
  var appVersion = envParams[2];

  
  // ----------------------------------------------
  // Build appUrl
  // ----------------------------------------------
  var protocol = "http://";
  var hostName = "paas-app-east-np.tsl.telus.com/";
  var appUrl = protocol + appName + "-" + envName + "." + hostName + appVersion;
  
  karate.log('Selected Application URL is:', appUrl);

  // ---------------------------------------
  // Define Test Application configuration
  // ---------------------------------------
  var config = {
    env: envName,
    appUrl: appUrl
  }

  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);
  
  

  
  return config;
}