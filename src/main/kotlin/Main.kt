package org.example

abstract class Observer(private val eventType: String) {

    abstract fun process(event: Event): String

    fun getEventType(): String {
        return eventType
    }

    fun sendSegmentEvent(eventName: String) {
        println("Sending SegmentEvent for event: $eventName")
    }
}

data class Event(val type: String)

interface Observable {
    fun addObserver(observer: Observer)
    fun notifyObservers(event: Event)
}

class EmailManager : Observable {
    private val observers = mutableListOf<Observer>()

    override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun notifyObservers(event: Event) {
        for (observer in observers) {
            observer.process(event)
        }
    }

    fun receiveEvent(event: Event) {
        // Notify only observers interested in this event type
        val filteredObservers = observers.filter { it.getEventType() == event.type }
        for (observer in filteredObservers) {
            //Segment Events can be returned from the process method
            //and passed into the sendSegmentEvent method
            val segmentEvent = observer.process(event)
            observer.sendSegmentEvent(segmentEvent)
        }

    }
}

class SignUpEventObserver(private val name: String) : Observer(eventType = "signup") {

    override fun process(event: Event): String {
        println("$name received event: ${event.type}")
        // Implement logic specific to this email
        // For example, fetch some data, do some transformation on event data etc., create the correct format for Segment Event
        // ...
        println("Processing Event: ${event.type}. Custom logic is being executed for this event type")
        val segmentEvent = "signupSegmentEvent"
        return segmentEvent
    }
}


class FDDEventObserver(private val name: String) : Observer(eventType = "fdd") {

    override fun process(event: Event): String {
        println("$name received event: ${event.type}")
        // Implement logic specific to this email
        // For example, fetch some data, do some transformation on event data etc., create the correct format for Segment Event
        // ...
        println("Processing Event: ${event.type}. Custom logic is being executed for this event type")
        // would be great if we can return the Segment Event from here
        val segmentEvent = "fddSegmentEvent"
        return segmentEvent
    }
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
        val signupEventObserver = SignUpEventObserver("Signup Event Observer")
        val fddEventObserver = FDDEventObserver("FDD Event Observer")

        // Register observers with the observable
        emailManager.addObserver(signupEventObserver)
        emailManager.addObserver(fddEventObserver)

        // EmailManager will notify the observers when it receives an event
        emailManager.receiveEvent(event)
    }
}


