package com.flyingsoftgames.pollfishplugin;

import com.pollfish.main.PollFish;
import com.pollfish.constants.Position; 

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.pollfish.interfaces.PollfishSurveyReceivedListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.interfaces.PollfishSurveyCompletedListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishUserNotEligibleListener;

import android.util.Log;

public class PollfishPlugin extends CordovaPlugin {
 
 private CallbackContext onPollfishSurveyReceivedCallback        = null;
 private CallbackContext onPollfishSurveyNotAvailableCallback    = null;
 private CallbackContext onPollfishSurveyCompletedCallback       = null;
 private CallbackContext onPollfishSurveyUserNotEligibleCallback = null;
 private CallbackContext onPollfishSurveyOpenedCallback          = null;
 private CallbackContext onPollfishSurveyClosedCallback          = null;
 
 private Position indicator_position = Position.TOP_LEFT;
 private String poll_api_key         = "";
 private int indicator_padding       = 0;
 private String init_type            = "init";
 
 private final String LOG_TAG = "PollfishPlugin";
 
 public boolean execute (String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
  if (("init".equals(action)) || ("customInit".equals(action))) {
   String string_indicator_position = inputs.optString(1);
   if      ("TOP_LEFT".equals    (string_indicator_position)) {indicator_position = Position.TOP_LEFT;    }
   else if ("MIDDLE_LEFT".equals (string_indicator_position)) {indicator_position = Position.MIDDLE_LEFT; }
   else if ("BOTTOM_LEFT".equals (string_indicator_position)) {indicator_position = Position.BOTTOM_LEFT; }
   else if ("TOP_RIGHT".equals   (string_indicator_position)) {indicator_position = Position.TOP_RIGHT;   }
   else if ("MIDDLE_RIGHT".equals(string_indicator_position)) {indicator_position = Position.MIDDLE_RIGHT;}
   else if ("BOTTOM_RIGHT".equals(string_indicator_position)) {indicator_position = Position.BOTTOM_RIGHT;}
   poll_api_key      = inputs.optString(0);
   indicator_padding = inputs.optInt(2);
   init_type         = action;
   doPollfishInit ();
   return true;
  }
  
  if ("showIndicator".equals(action)) {
   PollFish.show ();
  } else if ("hideIndicator".equals(action)) {
   PollFish.hide ();
  } else if ("setAttributesMap".equals(action)) {
   Map<String, String> map = new HashMap<String,String>();
   JSONObject map_list = inputs.optJSONObject(0);
   Iterator<?> keys = map_list.keys();
   while (keys.hasNext()) {
    String key   = (String)keys.next();
    String value = map_list.getString(key);
    map.put (key, value);
   }
   PollFish.setAttributesMap(map);
  } else if ("setEventListener".equals(action)) {
   setEventListener (inputs.optString(0), callbackContext);
  }
  return true;
 }
 
 void doPollfishInit () {
  cordova.getActivity().getWindow().getDecorView().postDelayed (new Runnable () {
   @Override public void run () {
    Log.d (LOG_TAG, poll_api_key);
    if ("init".equals(init_type)) {
     PollFish.init(cordova.getActivity(), poll_api_key, indicator_position, indicator_padding, onPollfishSurveyReceived, onPollfishSurveyNotAvailable, onPollfishSurveyCompleted, onPollfishOpened, onPollfishClosed, onUserNotEligible);
    } else if ("customInit".equals(init_type)) {
     PollFish.customInit(cordova.getActivity(), poll_api_key, indicator_position, indicator_padding, onPollfishSurveyReceived, onPollfishSurveyNotAvailable, onPollfishSurveyCompleted, onPollfishOpened, onPollfishClosed, onUserNotEligible);
    }
   }
  }, 50);
 }
 
 private void setEventListener (String eventType, CallbackContext callback) {
  Log.d (LOG_TAG, "setEventListener: " + eventType);
  if      ("received".equals       (eventType)) {onPollfishSurveyReceivedCallback        = callback;}
  else if ("notavailable".equals   (eventType)) {onPollfishSurveyNotAvailableCallback    = callback;}
  else if ("completed".equals      (eventType)) {onPollfishSurveyCompletedCallback       = callback;}
  else if ("usernoteligible".equals(eventType)) {onPollfishSurveyUserNotEligibleCallback = callback;}
  else if ("opened".equals         (eventType)) {onPollfishSurveyOpenedCallback          = callback;}
  else if ("closed".equals         (eventType)) {onPollfishSurveyClosedCallback          = callback;}
 }
 
 private PollfishSurveyReceivedListener onPollfishSurveyReceived = new PollfishSurveyReceivedListener () {
  public void onPollfishSurveyReceived (boolean isPlayfulSurvey, int surveyPrice) {
   Log.d (LOG_TAG, "onPollfishSurveyReceived");
   if (onPollfishSurveyReceivedCallback == null) return;
   try {
    JSONObject json_object = new JSONObject();
    json_object.put("isPlayfulSurvey", isPlayfulSurvey);
    json_object.put("surveyPrice"    , surveyPrice);
    onPollfishSurveyReceivedCallback.sendPluginResult (pluginResultKeep(json_object));
    Log.d (LOG_TAG, "onPollfishSurveyReceivedCallback is now: " + ((onPollfishSurveyReceivedCallback == null) ? "null." : "not null."));
   } catch (JSONException exception) {throw new RuntimeException(exception);}
  }
 };
 
 private PollfishSurveyNotAvailableListener onPollfishSurveyNotAvailable = new PollfishSurveyNotAvailableListener () {
  public void onPollfishSurveyNotAvailable () {
   Log.d (LOG_TAG, "onPollfishSurveyNotAvailable");
   if (onPollfishSurveyNotAvailableCallback != null) onPollfishSurveyNotAvailableCallback.sendPluginResult (pluginResultKeep());
  }
 };
 
 private PollfishSurveyCompletedListener onPollfishSurveyCompleted = new PollfishSurveyCompletedListener () {
  public void onPollfishSurveyCompleted (boolean isPlayfulSurvey, int surveyPrice) {
   if (onPollfishSurveyCompletedCallback == null) return;
   Log.d (LOG_TAG, "onPollfishSurveyCompleted");
   try {
    JSONObject json_object = new JSONObject();
    json_object.put("isPlayfulSurvey", isPlayfulSurvey);
    json_object.put("surveyPrice"    , surveyPrice);
    onPollfishSurveyCompletedCallback.sendPluginResult (pluginResultKeep(json_object));
   } catch (JSONException exception) {throw new RuntimeException(exception);}
  }
 };
 
 private PollfishUserNotEligibleListener onUserNotEligible = new PollfishUserNotEligibleListener () {
  public void onUserNotEligible () {
   Log.d (LOG_TAG, "onUserNotEligible");
   if (onPollfishSurveyUserNotEligibleCallback != null) onPollfishSurveyUserNotEligibleCallback.sendPluginResult (pluginResultKeep());
  }
 };
 
 private PollfishOpenedListener onPollfishOpened = new PollfishOpenedListener () {
  public void onPollfishOpened  () {
   Log.d (LOG_TAG, "onPollfishOpened");
   if (onPollfishSurveyOpenedCallback != null) onPollfishSurveyOpenedCallback.sendPluginResult (pluginResultKeep());
  }
 };
 
 private PollfishClosedListener onPollfishClosed = new PollfishClosedListener () {
  public void onPollfishClosed  () {
   Log.d (LOG_TAG, "onPollfishClosed");
   if (onPollfishSurveyClosedCallback != null) onPollfishSurveyClosedCallback.sendPluginResult (pluginResultKeep());
  }
 };
 
 private PluginResult pluginResultKeep () {
  PluginResult pluginResult = new PluginResult (PluginResult.Status.OK);
  pluginResult.setKeepCallback (true);
  return pluginResult;
 }
 
 private PluginResult pluginResultKeep (JSONObject message) {
  PluginResult pluginResult = new PluginResult (PluginResult.Status.OK, message);
  pluginResult.setKeepCallback (true);
  return pluginResult;
 }
}