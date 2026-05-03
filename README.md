# 🏃‍♂️ Aplikasi Catat Lari

## 📌 Deskripsi Aplikasi
Aplikasi **Catat Lari** adalah aplikasi mobile berbasis Android yang dibuat untuk membantu pengguna dalam mencatat aktivitas olahraga lari yang telah dilakukan.

Pengguna dapat menyimpan data lari seperti tanggal, jarak tempuh, dan durasi. Aplikasi ini juga dilengkapi dengan sistem login sehingga setiap pengguna hanya dapat melihat dan mengelola data miliknya sendiri.

---

## 🚀 Fitur Aplikasi

### 🔐 1. Autentikasi Pengguna
- Login dan Register
- Data pengguna tersimpan di database lokal
- Session login (tidak perlu login ulang jika sudah masuk)

---

### 🏃 2. Manajemen Data Lari
- Menambahkan data lari (tanggal, jarak, durasi)
- Menampilkan daftar riwayat lari
- Data ditampilkan berdasarkan user yang login

---

### 🗑️ 3. Hapus Data Lari
- Menghapus data lari yang sudah tersimpan

---

### ✏️ 4. Edit Data Lari
- Mengubah data lari yang sudah ada

---

### 👤 5. Profil Pengguna
- Menampilkan informasi pengguna
- Menampilkan ringkasan aktivitas (jumlah lari / total jarak)

---

### 🔁 6. Navigasi Aplikasi
- Perpindahan antar halaman (Login, Home, Add Run, Profile)
- Tombol kembali (back) di setiap halaman

---

### 🚪 7. Logout
- Keluar dari akun
- Kembali ke halaman login

---

## 🛠️ Tech Stack

- **Bahasa Pemrograman**: Kotlin  
- **Arsitektur**: MVVM (Model-View-ViewModel)  
- **Database**: Room Database (SQLite)  
- **UI**: XML Layout + ViewBinding  
- **Navigation**: Navigation Component  

---

## 🗂️ Struktur Fitur

- **User** → Mengelola data pengguna  
- **Run** → Menyimpan data aktivitas lari  
- **SessionManager** → Mengatur login session  
- **ViewModel** → Menghubungkan UI dan data  

---

## 👥 Tim Pengembang

| Nama Lengkap               | NPM         | Tanggung Jawab        |
|--------------------------|------------|----------------------|
| An Nisa' Fatmawati       | 24082010053 | Navigasi & Session   |
| Helen Risky Dwi Wahyuni  | 24082010054 | Manajemen Data Run (Database & Integrasi)       |
| Talitha Nabila Candra    | 24082010061 | Profil Pengguna      |
| Rindi Antika Qumalasari  | 24082010064 | Fitur Run            |

---

## 📈 Catatan Pengembangan

Aplikasi ini dikembangkan sebagai bagian dari tugas mata kuliah **Pemrograman Mobile**, dengan fokus pada:
- Implementasi arsitektur MVVM
- Penggunaan database lokal (Room)
- Manajemen state dan session pengguna

---
