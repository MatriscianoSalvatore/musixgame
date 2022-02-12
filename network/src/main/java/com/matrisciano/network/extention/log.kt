package com.matrisciano.network.extention

import android.util.Log
import com.matrisciano.network.BuildConfig

enum class LogType {
    /** Priority constant for the println method; use Log.v.*/
    VERBOSE,

    /** Priority constant for the println method; use Log.d.*/
    DEBUG,

    /** Priority constant for the println method; use Log.i.*/
    INFO,

    /** Priority constant for the println method; use Log.w.*/
    WARN,

    /** Priority constant for the println method; use Log.e.*/
    ERROR,
}

/**
 * Log extensions method.
 * @param msg - The message you would like logged.
 * @param logType (Optional) - The priority/type of this log message.
 * @param tag (Optional) - Used to identify the source of a log message.
 * @param throwable (Optional) - An exception to log.
 */
fun log(
    msg: String,
    logType: LogType = LogType.DEBUG,
    tag: String = "APP_TAG",
    throwable: Throwable? = null
) {
    if (BuildConfig.DEBUG) {
        if (throwable == null) {
            when (logType) {
                LogType.VERBOSE -> Log.v(tag, msg)
                LogType.DEBUG -> Log.d(tag, msg)
                LogType.INFO -> Log.i(tag, msg)
                LogType.WARN -> Log.w(tag, msg)
                LogType.ERROR -> Log.e(tag, msg)
            }
        } else {
            Log.e(tag, msg, throwable)
        }
    }
}
