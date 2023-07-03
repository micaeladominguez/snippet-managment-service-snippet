package com.example.snippetmanagmentservice.printscript

import interpreterUtils.Printer


class PrinterCollector : Printer {

    private val messages = ArrayList<String>()

    override fun print(value: String) {
        messages.add(value);
    }

    fun getMessages(): ArrayList<String> {
        return if (messages.isEmpty()) {
            arrayListOf("No messages")
        } else {
            ArrayList(messages)
        }
    }

}