package com.ikhoka.core

data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(error: Throwable? = null, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, error)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

fun <T> Resource<T>.isLoading(): Boolean {
    return this.status == Resource.Status.LOADING
}

fun <T> Resource<T>.isError(): Boolean {
    return this.status == Resource.Status.ERROR
}

fun <T> Resource<T>.isSuccess(): Boolean {
    return this.status == Resource.Status.SUCCESS
}
