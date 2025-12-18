/**
 * Contact Manager Console Application
 * Author: Brandon Ayton
 * Description: A contact manager application written in Kotlin in the console that demonstrates Variables,
 * Conditionals, Loops, Functions, Classes, Collections, Data Classes, and `when` expressions.
 */

import java.io.File
import java.io.IOException

// Data class
//Represents a single contact
data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val notes: String = ""
)

// Main class
class ContactManager {
    // List to store contacts (Collections stretch challenge)
    private val contacts = mutableListOf<Contact>()
    private var nextId = 1
    private val dataFile = "contacts.txt"

    init {
        loadContactsFromFile()
    }

    // Functions

    //Adds a new contact to the list.
    fun addContact() {
        println("\n--- Add New Contact ---")
        print("Enter name: ")
        val name = readlnOrNull()?.trim() ?: ""
        print("Enter phone: ")
        val phone = readlnOrNull()?.trim() ?: ""
        print("Enter email: ")
        val email = readlnOrNull()?.trim() ?: ""
        print("Enter notes (optional): ")
        val notes = readlnOrNull()?.trim() ?: ""

        if (name.isBlank() || phone.isBlank() || email.isBlank()) {
            println("Name, phone, and email are required.")
            return
        }

        val newContact = Contact(nextId++, name, phone, email, notes)
        contacts.add(newContact)
        saveContactsToFile()
        println("Contact added successfully (ID: ${newContact.id})")
    }


    //Displays all contacts
    fun viewContacts() {
        println("\n--- All Contacts ---")
        if (contacts.isEmpty()) {
            println("No contacts found.")
            return
        }
        contacts.forEach { contact ->
            println("ID: ${contact.id} | Name: ${contact.name} | Phone: ${contact.phone} | Email: ${contact.email}")
            if (contact.notes.isNotBlank()) println("   Notes: ${contact.notes}")
        }
    }

    //Searches contacts by name or phone.
    fun searchContacts() {
        println("\n--- Search Contacts ---")
        print("Search by (1) Name or (2) Phone: ")
        val choice = readlnOrNull()?.toIntOrNull()
        print("Enter search term: ")
        val term = readlnOrNull()?.trim()?.lowercase() ?: ""

        val results = when (choice) {
            1 -> contacts.filter { it.name.lowercase().contains(term) }
            2 -> contacts.filter { it.phone.contains(term) }
            else -> {
                println("Invalid choice.")
                return
            }
        }

        if (results.isEmpty()) {
            println("No matching contacts.")
        } else {
            println("Found ${results.size} contact(s):")
            results.forEach { println("  ${it.name} - ${it.phone}") }
        }
    }

    //Edits an existing contact by ID
    fun editContact() {
        println("\n--- Edit Contact ---")
        print("Enter contact ID to edit: ")
        val id = readlnOrNull()?.toIntOrNull()
        val contact = contacts.find { it.id == id }

        if (contact == null) {
            println("Contact not found.")
            return
        }

        println("Editing: ${contact.name} (Leave blank to keep current value)")
        print("New name [${contact.name}]: ")
        val newName = readlnOrNull()?.trim()
        print("New phone [${contact.phone}]: ")
        val newPhone = readlnOrNull()?.trim()
        print("New email [${contact.email}]: ")
        val newEmail = readlnOrNull()?.trim()
        print("New notes [${contact.notes}]: ")
        val newNotes = readlnOrNull()?.trim()

        val updatedContact = contact.copy(
            name = newName?.takeIf { it.isNotBlank() } ?: contact.name,
            phone = newPhone?.takeIf { it.isNotBlank() } ?: contact.phone,
            email = newEmail?.takeIf { it.isNotBlank() } ?: contact.email,
            notes = newNotes ?: contact.notes
        )

        val index = contacts.indexOf(contact)
        contacts[index] = updatedContact
        saveContactsToFile()
        println("Contact updated.")
    }

    //Deletes a contact by ID.
    fun deleteContact() {
        println("\n--- Delete Contact ---")
        print("Enter contact ID to delete: ")
        val id = readlnOrNull()?.toIntOrNull()
        val contact = contacts.find { it.id == id }

        if (contact != null) {
            contacts.remove(contact)
            saveContactsToFile()
            println("Contact deleted.")
        } else {
            println("Contact not found.")
        }
    }

    // File operations

    //Saves contacts to a text file.
    private fun saveContactsToFile() {
        try {
            File(dataFile).writeText(
                contacts.joinToString("\n") { contact ->
                    "${contact.id}|${contact.name}|${contact.phone}|${contact.email}|${contact.notes}"
                }
            )
        } catch (e: IOException) {
            println("Could not save contacts to file.")
        }
    }

    //Loads contacts from a text file.
    private fun loadContactsFromFile() {
        val file = File(dataFile)
        if (!file.exists()) return

        try {
            file.readLines().forEach { line ->
                val parts = line.split("|")
                if (parts.size >= 5) {
                    contacts.add(
                        Contact(
                            id = parts[0].toInt(),
                            name = parts[1],
                            phone = parts[2],
                            email = parts[3],
                            notes = parts[4]
                        )
                    )
                    nextId = maxOf(nextId, parts[0].toInt() + 1)
                }
            }
        } catch (e: Exception) {
            println("Could not load contacts from file.")
        }
    }

    //Displays the main menu and handles user input.
    fun run() {
        var running = true

        while (running) {
            println("\nCONTACT MANAGER")
            println("1. Add Contact")
            println("2. View All Contacts")
            println("3. Search Contacts")
            println("4. Edit Contact")
            println("5. Delete Contact")
            println("6. Exit")
            print("Choose an option (1-6): ")

            when (readlnOrNull()?.toIntOrNull()) {
                1 -> addContact()
                2 -> viewContacts()
                3 -> searchContacts()
                4 -> editContact()
                5 -> deleteContact()
                6 -> {
                    println("Thank you for using the Kotlin Contact manager.")
                    running = false
                }
                else -> println("Invalid option. Please choose 1-6.")
            }
        }
    }
}

// Main function
fun main() {
    println("=== Kotlin Contact Manager ===")
    val manager = ContactManager()
    manager.run()
}
