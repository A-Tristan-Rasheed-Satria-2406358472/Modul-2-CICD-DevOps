# Refleksi Module 3 (3.1)

(relfleksi modul 1 dan 2 dibawah ini)

### 1) Explain what principles you apply to your project!

- **SRP (Single Responsibility Principle)** Setiap class punya satu tanggung jawab utama. ProductController hanya mengurus endpoint Product, CarController hanya endpoint Car, service fokus ke business logic, repository fokus ke akses data sementara Model hanya menyimpan data/invarian sederhana
- **OCP (Open/Closed Principle)** Service dibangun agar bisa diperluas tanpa mengubah kode inti. Ada ProductServiceImpl dimana dia bergantung ke ProductDataStore, jadi kalau mau ganti penyimpanan data (misal dari in-memory ke database), cukup tambah implementasi datastore baru.
- **LSP (Liskov Substitution Principle)** Tidak ada lagi pewarisan yang memaksa relasi yang tidak tepat. Car flow dipisah ke CarController, sehingga tidak ada subclass yang melanggar perilaku base class.
- **ISP (Interface Segregation Principle)** Interface dipisah sesuai kebutuhan domain, Di ProductDataStore dan CarDataStore, supaya client-nya tidak bergantung pada method yang tidak dipakai
- **DIP (Dependency Inversion Principle)** High-level module bergantung ke abstraksi, bukan bergantung ke implementasi. Di ProductServiceImpl dan CarServiceImpl menerima dependency berupa interface melalui constructor injection

### 2) Explain the advantages of applying SOLID principles to your project with examples.

- Lebih mudah maintenance-nya. Karena Product dan Car dipisah, perubahan fitur Car tidak merusak flow Product.
- Lebih mudah dikembangin lagi (extensible). Dengan OCP + DIP, bisa aja kalau mau menambah implementasi repository baru tanpa perlu ubah logic service yang sudah ada
- Lebih gampang di-test. Karena service bergantung pada interface, dependency bisa di-mock saat unit testing. Membuat test lebih cepat dan terisolasi
- Coupling antar layer menurun. Controller,Service, dan Repository terhubung lewat kontrak yang jelas, jadi refactor di satu layer tidak menimbulkan banyak perubahan berantai.
- Kode lebih scalable untuk fitur berikutnya, Saat nambah entity/fitur baru, pola yang sama bisa dipakai ulang tanpa membuat class lama terlalu gemuk

## 3) Explain the disadvantages of not applying SOLID principles to your project with examples.

- Class menjadi terlalu gede dan sulit dipahami. Jika Product dan Car ditangani dalam class yang sama, class tersebut punya banyak alasan untuk berubah dan rawan bug

- Perubahan kecil bisa ngebuat bug di modul lain. Tanpa SRP/LSP, modifikasi di flow Car berpotensi mengganggu endpoint Product

- Sulit kalau mau ganti teknologi/data source. Tanpa OCP/DIP, perpindahan dari in-memory repository ke database menuntut perubahan di banyak class sekaligus

- Testing jadi lebih rumit. Tanpa abstraksi, class sulit di-mock sehingga unit test bisa dibilang berubah jadi integration test yang lebih berat

- Dependency antar class makin ketat. Tanpa ISP/DIP, banyak class menjadi saling terikat langsung ke implementasi detail

# Refleksi Module 2 (4.2)

(relfleksi modul 1 dibawah ini)

1. Selama exercise ini, saya memperbaiki beberapa isu code quality. Pertama, pelanggaran PMD pada test karena penggunaan setAccessible() saya perbaikin dengan mengganti strategi setup data test agar tidak memakai refleksi, agar aman dan lolos rule static analysis. Kedua, saya merapikan kualitas test dengan menghapus import yang tidak terpakai, merapikan urutan import agar konsisten, serta menyesuaikan pemanggilan API Selenium yang deprecated supaya warningnya berkurang dan lebih maintanable. Ketiga, saya memastikan coverage branch yang sebelumnya belum tertutup menjadi teruji dengan menambah test case yang spesifik untuk skenario null, not-found, dan found pada repository. Perbaikan ini membantu proses review kode jadi lebih cepat karena temuan kualitas sudah berkurang dari awal pipeline. Selain itu, perubahan tadi bikin basis test lebih rapi untuk pengembangan fitur berikutnya supaya tidak menambah technical debt.

2. Menurut saya, implementasi saat ini sudah memenuhi definisi Continuous Integration karena setiap push/pull request otomatis menjalankan proses build, unit test, PMD, dan pembuatan laporan coverage. Mekanisme ini memastikan perubahan kode cepat tervalidasi dan masalah kualitas bisa terdeteksi lebih awal sebelum merge. Untuk Continuous Deployment, alurnya juga sudah terpenuhi ke environment PaaS karena deploy terjadi otomatis dari branch utama melalui integrasi Render (auto deploy langsung dari render-nya), sehingga perubahan yang lolos pipeline dapat langsung dipublikasikan tanpa langkah manual tambahan. Dengan alur ini, feedback loop jadi lebih pendek karena status kualitas dan kelulusan test langsung terlihat setelah commit. Menurut saya ini juga mengurangi risiko human error saat release karena langkah manual deployment hampir tidak ada. Selain itu, quality check dari CodeQL - Code Quality / Analyze (java-kotlin) juga berhasil. jadi analisis kualitas dan potensi issue di level static analysis makin valid.

# Refleksi 1

Selama mengimplementasikan dua fitur (edit dan delete) di teraapkan juga beberapa prinsip clean code seperti Single Responsibility (memisahkan controller, service, dan repository), penamaan method yang jelas findById, update, deleteById. Selain itu, ada pemisahan layer sehingga alur request -> logika -> akses data mudah diikuti. Pada bagian template, struktur Thymeleaf dipisahin sehingga view tetap ringkas dan tanggung jawab presentation terjaga. kodenya dibuat sederhana dan mudah dibaca,agar lebih mudah saat pembuatan unit test dan review

Dari sisi secure coding, saya menerapkan redirect absolut untuk menghindari masalah routing relatif dan menggunakan parameter bertipe pada @PathVariable sehingga Spring melakukan validasi tipe input dasar. Namun masih ada yang perlu diperbaiki, misalnya operasi delete dieksekusi lewat GET (tanpa POST/CSRF protection) yang bisa dibilang berisiko; ini solusinya menggunakan POST dengan CSRF token. abis itu mungkin global error handling untuk kasus error umum sama validasi input kalau mau lebih lengkap

# Refleksi 2

1. Setelah menulis unit test saya merasa lebih yaking (seharusnya) terhadap bagian yang sudah diuji karena regresi bakal jadi lebih mudah terdeteksi. Unit test fokusnya pada cakupan perilak dimana setiap alur yang penting harus punya test (positif, negatif, dan edge case). Untuk memastikan testnya cukup, bisa dengan gabungkan unit tests dengan integration/functional tests, pikirkan juga tentang skenario edge case, dan kita dapat menggunakan metrik seperti code coverage untuk menemukan area yang belum teruji. menurut saya code coverage yang sudah 100% belum berarti berarti kode tidak ada bug, coverage cuman memperlihatkan baris yang dieksekusi oleh test, bukan kualitas atau kecakupan semua kondisi logika

2. Jika membuat kelas functional test baru yang menyalin setup dan instance variable dari test lain, hal ini berpotensi menurunkan kualitas kode karena duplikasi, sehingga maintenance menjadi lebih sulit dan ada risiko ketidakkonsistenan saat setup berubah. Untuk memperbaikinya, ekstrak konfigurasi dan inisialisasi ke superclass (seperti BaseFunctionalTest) atau helper/fixture bersama. Gunakan @BeforeEach untuk inisialisasi yang konsisten, terapkan Page Object Pattern agar interaksi UI terenkapsulasi, dan gunakan parameterized tests bila hanya input yang berubah. Langkah-langkah ini dapat mengurangi duplikasi, mempermudah perubahan, dan meningkatkan keterbacaan serta maintainability test suite.

# Refleksi Module 1

Selama mengimplementasikan dua fitur (edit dan delete) di teraapkan juga beberapa prinsip clean code seperti Single Responsibility (memisahkan controller, service, dan repository), penamaan method yang jelas findById, update, deleteById. Selain itu, ada pemisahan layer sehingga alur request -> logika -> akses data mudah diikuti. Pada bagian template, struktur Thymeleaf dipisahin sehingga view tetap ringkas dan tanggung jawab presentation terjaga. kodenya dibuat sederhana dan mudah dibaca,agar lebih mudah saat pembuatan unit test dan review

Dari sisi secure coding, saya menerapkan redirect absolut untuk menghindari masalah routing relatif dan menggunakan parameter bertipe pada @PathVariable sehingga Spring melakukan validasi tipe input dasar. Namun masih ada yang perlu diperbaiki, misalnya operasi delete dieksekusi lewat GET (tanpa POST/CSRF protection) yang bisa dibilang berisiko; ini solusinya menggunakan POST dengan CSRF token. abis itu mungkin global error handling untuk kasus error umum sama validasi input kalau mau lebih lengkap

# Refleksi 2

1. Setelah menulis unit test saya merasa lebih yaking (seharusnya) terhadap bagian yang sudah diuji karena regresi bakal jadi lebih mudah terdeteksi. Unit test fokusnya pada cakupan perilak dimana setiap alur yang penting harus punya test (positif, negatif, dan edge case). Untuk memastikan testnya cukup, bisa dengan gabungkan unit tests dengan integration/functional tests, pikirkan juga tentang skenario edge case, dan kita dapat menggunakan metrik seperti code coverage untuk menemukan area yang belum teruji. menurut saya code coverage yang sudah 100% belum berarti berarti kode tidak ada bug, coverage cuman memperlihatkan baris yang dieksekusi oleh test, bukan kualitas atau kecakupan semua kondisi logika

2. Jika membuat kelas functional test baru yang menyalin setup dan instance variable dari test lain, hal ini berpotensi menurunkan kualitas kode karena duplikasi, sehingga maintenance menjadi lebih sulit dan ada risiko ketidakkonsistenan saat setup berubah. Untuk memperbaikinya, ekstrak konfigurasi dan inisialisasi ke superclass (seperti BaseFunctionalTest) atau helper/fixture bersama. Gunakan @BeforeEach untuk inisialisasi yang konsisten, terapkan Page Object Pattern agar interaksi UI terenkapsulasi, dan gunakan parameterized tests bila hanya input yang berubah. Langkah-langkah ini dapat mengurangi duplikasi, mempermudah perubahan, dan meningkatkan keterbacaan serta maintainability test suite.
