
# Android App - Note Management

The Notes Manager app combines user authentication with a feature-rich notes management system. It provides functionalities like sign-in, sign-up, and secure note management, ensuring user data is protected.

## Features
• **User Authentication**: Sign-in and sign-up functionalities.  
• **Two-Factor Authentication**: Secure user login with an additional verification step.  
• **Forgot Password**: Recover account credentials easily.  
• **Notes Management**: Create, edit, delete, and view notes.  
• **Secure Data Handling**: Implements encryption and secure storage.  
• **Background Work**: Automated tasks using workers.


# Key Components
### Authentication and Security
• **LoginActivity**: Handles user login.<br>
• **SignUpActivity**: Registers new users.<br>
• **ForgotPasswordActivity**: Assists users in password recovery.<br>
• **TwoFactorAuthUtils and Verify2FACodeActivity**: Implements two-factor authentication.<br>
• **HashUtils and KeystoreUtils**: Provides secure password hashing and key management.<br>

### Notes Management
• **ManageNotesActivity**: Central screen for managing all notes.<br>
• **AddOrEditNoteActivity**: Enables creating and editing notes.<br>
• **NotesAdapter**: Manages notes display in a list or grid.<br>
• **Note**: Represents individual note data.<br>

### Utilities and Background Tasks
• **EmailSender**: Sends email notifications (e.g., for password recovery).<br>
• **MyBackgroundWorker**: Handles background tasks like syncing.<br>
• **OneTimeWorkRequest**: Schedules specific tasks.<br>
• **TimeSetReceiver**: Monitors and handles time-related events.<br>

## Contributors  
- [@VleraIslami](https://github.com/VleraIslami) - Vlera Islami  
- [@Adriansopii](https://github.com/Adriansopii) - Adrian Sopi  
- [@VleraLuma](https://github.com/VleraLuma) - Vlera Luma  
- [@vesax](https://github.com/vesax) - Vesa Zhitia  

### Data Storage
• **SQLiteHelper**: Manages local database storage.<br>
• **PasswordManager**: Stores and retrieves encrypted passwords.<br>


