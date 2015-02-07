Pollfish plugin.
==================

Implement the Pollfish API with this plugin.


Donations!
----------------------
Please feed Tiny Tim.

Gratipay / Gittip: https://gratipay.com/agamemnus/

Paypal: agamemnus at flyingsoftgames dot com


Install
----------------------
````
cordova plugin add https://github.com/agamemnus/cordova-plugin-pollfish
````

Usage / Function List
----------------------

init: Initialize the plugin and/or request a new survey.
````
window.plugins.PollfishPlugin.init ({
 use_indicator : true,       // Set to true if you want to show the survey with an initial small indicator button. Set to false if you want to start it full-screen.
 api_key       : "1234",     // Set the Pollfish API key.
 pos           : "TOP_LEFT", // Set the position of the survey indicator, as a string. Possible values: "TOP_LEFT", "MIDDLE_LEFT", "BOTTOM_LEFT", "TOP_RIGHT", "MIDDLE_RIGHT", and "BOTTOM_RIGHT".
 padding       : 0           // Set the offset of the pollfish indicator from its designated position ("pos", above).
}}
````
<br/>
showIndicator & hideIndicator: Programmatically show / hide the indicator / window.
````
window.plugins.PollfishPlugin.showIndicator ()
window.plugins.PollfishPlugin.hideIndicator ()
````
<br/>
setAttributesMap: Set an attribute map for Pollfish to better understand user data.
E.G.: ``setAttributesMap({FacebookID: "1234", TwitterID: "10sde"})``.
````
window.plugins.PollfishPlugin.setAttributesMap (map)
````
<br/>
setEventListener: Set a single event listener for a specific event type. This event listener will not expire when it is fired.
E.G.: ``setEventListener(received, function () {console.log ("Got a survey!")})``
````
 // eventType must be "received", "notavailable", "completed", "usernoteligible", "opened", "closed".
 // "received"        : A Pollfish survey was received by the app. The callback outputs whether it is a "playful survey" and the price in USD of completing the survey.
 // "notavailable"    : No survey can be received, so it's not available.
 // "completed"       : The survey is completed by the user.
 // "usernoteligible" : After accepting to take a survey the survey may end if it finds out that the user is not eligible. (i.e.: a survey for pregnant women, but the survey taker says they are not a pregnant woman.)
 // "opened"          : A survey was opened.
 // "closed"          : A survey was closed.
window.plugins.PollfishPlugin.setEventListener (eventType, callback)
````
