package com.example.snippetmanagmentservice.redis.consumer

import configurationLinter.ConfigClassesLinter

data class ProductCreated(val snippet: String, val rules: ArrayList<ConfigClassesLinter>)