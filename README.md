# TaskWhiz – AI Smart Task Organizer

**TaskWhiz** is a native Android app that helps users convert unstructured, natural language text into structured to-do tasks using the Hugging Face **Llama 3.2-1B** model.

Users can type quick notes like:  
- “maybe submit tax documents by end of this week”  
- “schedule dentist appointment next month”  

The app intelligently extracts:
- Task Title  
- Due Date  
- Priority Level  
- Category (e.g., Work, Personal, Errands)



---

## Tech Stack

### Architecture & UI
- Kotlin  
- Clean Architecture  + MVVM
- Jetpack Compose  
- Room Database  
- Hilt (Dependency Injection)  
- Kotlin Coroutines & Flow

### AI & Networking
- `llama.cpp` – Local model inference  
- Hugging Face Llama 3.2-1B (GGUF format)  
- Retrofit – REST API communication

### Testing
- JUnit  
- Mockito  
- Espresso

### Developer Tools
- GitHub Actions – CI/CD workflows

---

