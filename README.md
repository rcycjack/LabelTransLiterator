# 📚 LabelTransLiterator

**LabelTransLiterator** is a lightweight Android library that maps the **first character of a string** to an **A–Z alphabetical label**. If no Latin alphabet letter applies, it returns **`#`**. It supports many scripts and languages by transliterating based on the user's Android system language settings.

---

## ✨ Features

- 🅰️ Maps any string's first letter to A–Z
- 🌍 Supports multiple languages and scripts
- 🔡 Falls back to `#` for non-alphabetic characters
- 📱 Ideal for apps that categorize names, contacts, or app titles

---

## 🌐 Supported Languages

This library uses ICU4J transliteration and supports:

- English
- Chinese
- Japanese (Hiragana, Katakana)
- Korean
- Arabic
- Russian (Cyrillic)
- Greek
- Hebrew
- Romanian
- And more...

---

## 🚀 Installation

### Using [JitPack](https://jitpack.io)

1. Add the JitPack repository in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}


2. Add the library dependency:

```kotlin
dependencies {
    implementation("com.github.rcycjack:labeltransliterator:0.1.0")
}
```

---

## 📦 Usage

### 1. Initialize

```kotlin
val transliterator = LabelTransform()
```

### 2. Get Alphabet Label

```kotlin
val label1 = transliterator.getRepresentLetter("zip")     // Z
val label2 = transliterator.getRepresentLetter("北京")     // B
val label3 = transliterator.getRepresentLetter("Москва")  // M
val label4 = transliterator.getRepresentLetter("123")     // #
```

---

## 🧪 Example Output

| Input       | Output |
| ----------- | ------ |
| `"apple"`   | `A`    |
| `"zip"`     | `Z`    |
| `"Москва"`  | `M`    |
| `"Αθήνα"`   | `A`    |
| `"北京"`      | `B`    |
| `"שלום"`    | `S`    |
| `"こんにちは"`   | `K`    |
| `"123test"` | `#`    |

---


## 🙌 Contributions

PRs and issues are welcome! Feel free to fork and improve this library to support more languages or customization features.
