package com.example.pppbnotesapp

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object MyExecutorService {
    val executorService: ExecutorService = Executors.newSingleThreadExecutor()
}