# Arsitektur Frontend: E-Commerce Otaku Store

Dokumen ini merincikan spesifikasi untuk antarmuka pengguna (Frontend). Antarmuka didesain sedemikian rupa untuk menampilkan katalog produk Jepang secara premium, namun tetap menjamin kenyamanan interaksi jangka panjang bagi para klien (*user-retention*). 

## 1. Tumpukan Teknologi (Tech Stack) Inti
- **Framework & Build Tool:** **React.js** dikombinasikan secara penuh dengan bundler **Vite**. Keunggulannya adalah kecepatan rendering, yang akan sangat dibutuhkan ketika memproses modul-modul interaktif interaktif semacam *Custom Outfit Canvas Editor*.
- **Routing:** **React Router DOM**, menjamin transisi perpindahan dari halaman Katalog ke detail 3D Action Figure secara *"Single-Page"* tanpa ada jeda atau layar putih membosankan *(loading bar)*.
- **Pengaturan State:** Memanfaatkan **Context API** asli React (atau Zustand).

## 2. Strategi Estetika UI & CSS (Visual Unik, Ikonik & Nyaman)
Visual situs berlandaskan *Vanilla CSS* canggih dan menggunakan variabel akar sentralisasi warna (CSS Variables).

**Konsep Tema Warna Utama: "Midnight Neon Elegance"**
- **Basis Nyaman & Ramah Mata (Long-term Access):** Warna *background* utama ditarik menjauhi hitam murni yang membosankan dan diganti dengan spektrum rona *Dark Blue/Obsidian* (misalnya `#0d1117` atau `#0b0f19`). Latar gelap memudarkan kontras cahaya pada monitor, sehingga pelanggan tidak sakit mata meski berjam-jam berburu koleksi Manga dan Action Figure.
- **Aksen Unik & Mudah Diingat (Memorable):** Untuk menciptakan jati diri brand yang kuat, tombol interaktif (seperti *Add to Cart*), label Diskon promo, serta garis batas kustomisasi *Outfit* akan mengusung percikan spektrum warna cerah mencolok. Kita akan menggunakan gradasi holografik atau warna solid tajam—seperti *Cyber Cyan* (`#00F0FF`) tersanding pelan bersama *Neon Magenta*. Aksen ini eksentrik namun diposisikan elegan sehingga tidak terkesan murahan.
- **Tekstur Modern (Glassmorphism):** Menaruh filter elemen kaca berpendar di belakang kartu navigasi gantung (*Floating Navigation*), menegaskan nuansa "mahal".

## 3. Kompatibilitas Seluruh Perangkat (Omni-Device Responsiveness)
Setiap incinya diarahkan untuk responsif:
- **Pendekatan Adaptif (Mobile First):** Desain disimulasikan dari format HP terlebih dahulu. Mengingat sebagian pelanggan suka berbelanja via ponsel pintar, tap area tombol akan diatur sangat ramah ibu jari.
- **Struktur Flex & Grid Cerdas:** Menggunakan properti `grid-template-columns: repeat(auto-fit, minmax(200px, 1fr))` pada Vanilla CSS. Artinya, tampilan daftar 100 Katalog DVD BluRay bisa tampil 2 baris bersandingan vertikal dengan lues di bodi _smartphone_, dan otomatis memekar menjadi daftar 5 etalase lebar simetris tatkala diputar (*landscape*) atau diakses dari Tablet/Layar Desktop 4K.
- **Editor Outfit Resilensi Lintas Layar:** Ruang interaktif *Custom Outfit* dipastikan tidak akan menabrak margin batas layar di ponsel pintar. Gambar *mockup* baju akan mengusung *scaling* otomatis sehingga alat sentuh *drag & drop* (menggeser desain logo custom) tetap bekerja sangat proporsional di layar apa pun.
