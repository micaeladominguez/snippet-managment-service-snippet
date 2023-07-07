package com.example.snippetmanagmentservice.redis.consumer

import com.example.snippetmanagmentservice.snippet.Snippet
import configurationLinter.ConfigClassesLinter

data class ProductCreated(val snippet: Snippet, val rules: ArrayList<ConfigClassesLinter>)