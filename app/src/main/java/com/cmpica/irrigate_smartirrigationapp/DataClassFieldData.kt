package com.cmpica.irrigate_smartirrigationapp

data class DataClassFieldData(var user_id: String, var field_id: String,
                              var field_humdity: String, var field_start: String,
                              var field_stoppar: String, var field_stoptot: String,
                              var field_water: String, var field_waterflow: String,
                              var field_type: String) {

    constructor():this("","",
        "","","",
        "","","",
        "")
}