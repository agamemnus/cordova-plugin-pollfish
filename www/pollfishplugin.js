module.exports = function () {
 var exports = {}
 exports.init = function (init) {
  var use_indicator = ((typeof init.use_indicator != "undefined") ? init.use_indicator : false)
  var function_to_run = (use_indicator ? "init" : "customInit")
  cordova.exec (undefined, undefined, "PollfishPlugin", function_to_run, [init.api_key, init.pos || "TOP_LEFT", init.padding || 0])
 }
 
 exports.showIndicator = function () {
  cordova.exec (undefined, undefined, "PollfishPlugin", "showIndicator", [])
 }
 
 exports.hideIndicator = function () {
  cordova.exec (undefined, undefined, "PollfishPlugin", "hideIndicator", [])
 }
 
 // "map" is a key/value array, e.g.: {FacebookID: "1234", TwitterID: "10sde"}.
 exports.setAttributesMap = function (map) {
  cordova.exec (undefined, undefined, "PollfishPlugin", "setAttributesMap", [map])
 }
 
 // eventType must be "received", "notavailable", "completed", "usernoteligible", "opened", "closed".
 // "received"        : A Pollfish survey was received by the app. The callback outputs whether it is a "playful survey" and the price in USD of completing the survey.
 // "notavailable"    : No survey can be received, so it's not available.
 // "completed"       : The survey is completed by the user.
 // "usernoteligible" : After accepting to take a survey the survey may end if it finds out that the user is not eligible. (i.e.: a survey for pregnant women, but the survey taker says they are not a pregnant woman.)
 // "opened"          : A survey was opened.
 // "closed"          : A survey was closed.
 
 exports.setEventListener = function (eventType, callback) {
  cordova.exec (callback, undefined, "PollfishPlugin", "setEventListener", [eventType])
 }
 
 return exports
} ()