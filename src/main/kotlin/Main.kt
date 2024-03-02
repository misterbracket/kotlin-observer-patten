package org.example

abstract class Observer {
    abstract fun update(event: Event)
    abstract fun getEventType(): String
    fun sendSegmentEvent(eventName: String) {
        println("Sending SegmentEvent for event: $eventName")
    }
}

data class Event(val type: String)

interface Observable {
    fun addObserver(observer: Observer)
    fun removeObserver(observer: Observer)
    fun notifyObservers(event: Event)
}

class EmailManager : Observable {
    private val observers = mutableListOf<Observer>()

    override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers(event: Event) {
        for (observer in observers) {
            observer.update(event)
        }
    }

    fun receiveEvent(event: Event) {
        // Notify only observers interested in this event type
        val filteredObservers = observers.filter { it.getEventType() == event.type }
        for (observer in filteredObservers) {
            observer.update(event)
        }
    }
}

class KafkaEventObserver(private val name: String, private val eventType: String) : Observer() {
    override fun update(event: Event) {
        println("$name received event: ${event.type}")
        // Implement logic specific to this email
        // For example, fetch some data, do some transformation on event data etc., create the correct format for Segment Event
        // ...
        when (event.type) {
            "signup" -> {
                println("Processing Event: ${event.type}. Custom logic is being executed for this event type")
            }

            "fdd" -> {
                println("Processing Event: ${event.type}. Custom logic is being executed for this event type")
            }
        }

        this.sendSegmentEvent(event.type)
    }

    override fun getEventType(): String = eventType

}

fun main() {
    while (true) {
        val eventStr = readln()
        val event = if (eventStr == "exit") {
            break
        } else {
            Event(eventStr)
        }

        println("Received Event: $eventStr")

        // Create an observable (subject)
        val emailManager = EmailManager()

        // Create observers
        val signupEventObserver = KafkaEventObserver("Signup Event Observer", "signup")
        val fddEventObserver = KafkaEventObserver("FDD Event Observer", "fdd")

        // Register observers with the observable
        emailManager.addObserver(signupEventObserver)
        emailManager.addObserver(fddEventObserver)

        // EmailManager will notify the observers when it receives an event
        emailManager.receiveEvent(event)
    }
}


