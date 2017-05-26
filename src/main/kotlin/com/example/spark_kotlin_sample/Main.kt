package com.example.spark_kotlin_sample

import com.example.spark_kotlin_sample.user.UserDao
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.log4j.BasicConfigurator
//import org.apache.log4j.PropertyConfigurator
import spark.Request
import spark.Spark.*

fun main(args: Array<String>) {
    BasicConfigurator.configure()

    exception(Exception::class.java){e, req, res -> e.printStackTrace()}
    val userDao = UserDao()

    path("/users") {

        get("") { req, res ->
            jacksonObjectMapper().writeValueAsString(userDao.users)
        }

        get("/:id") { req, res ->
            userDao.findById(req.params("id").toInt())
        }

        get("/email/:email") { req, res ->
            userDao.findByEmail(req.params("email"))
        }

        post("/create") { req, res ->
            userDao.save(name = req.qp("name"), email = req.qp("email"))
            res.status(201)
            "ok"
        }

        patch("/update/:id") { req, res ->
            userDao.update(
                    id = req.params("id").toInt(),
                    name = req.qp("name"),
                    email = req.qp("email")
            )
            "ok"
        }

        delete("/delete/:id") { req, res ->
            userDao.delete(req.params("id").toInt())
            "ok"
        }

    }

    userDao.users.forEach(::println)

 }

// add "qp()" alias for "queryParams()" on Request object
fun Request.qp(key: String): String = this.queryParams(key)

fun printSum(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}

