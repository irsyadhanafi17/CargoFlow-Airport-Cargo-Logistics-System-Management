# CargoFlow-Airport-Cargo-Logistics-System-Management

# SLIDE PPT
=== SLIDE 1: JUDUL UTAMA
` Judul: CargoFlow: Airport Cargo Logistics System
` Anggota Kelompok: [Nama Lu, Nama Temen Lu]
` Program Studi: S1 Computer Science, Universitas Ary Ginanjar

=== SLIDE 2: PENJELASAN MANFAAT APLIKASI (Bobot: 10)
` Headline: Mengapa Bandara Butuh CargoFlow?
` Poin Utama:
- Otomatisasi Logistik: Menggantikan penyusunan manifes kargo manual bandara yang rawan salah kalkulasi.
- Prioritas Akurat: Menjamin barang darurat (Critical seperti medis/dokumen) selalu masuk lambung pesawat pertama kali.
- Safety & Efisiensi: Optimalisasi beban overload pesawat berdasarkan kalkulasi berat kargo secara real-time.

=== SLIDE 3: FITUR-FITUR APLIKASI (Bobot: 10)
` Headline: 4 Fitur Utama CargoFlow
` Poin Utama:
1. Cargo Registration: Form input ID kargo, nama barang, tujuan, berat, dan level urgensi.
2. Priority Loading (Dequeue): Mengeluarkan kargo terpenting otomatis ke pesawat.
3. Emergency Loading Undo: Membatalkan masuknya kargo terakhir jika ada perubahan manifes pilot.
4. Multi-Column Search & Weight Sorting: Melacak kargo instan dan mengurutkan beban.

=== SLIDE 4: STRUKTUR DATA 1 – PRIORITY QUEUE HEAP (Bobot: 20)
` Headline: Manajemen Antrian Terminal via Max-Heap Array
` Alasan Pemilihan: Kasus kargo bandara tidak bisa pakai Antrian Biasa (FIFO) karena barang darurat harus memotong antrian kargo reguler.
` Kompleksitas Komputasi (Time Complexity):
1. Insert (Enqueue): $\mathbf{O(\log n)}$ -> Efisien melalui prosedur Heapify-Up
2. Remove Max (Dequeue): $\mathbf{O(\log n)}$ -> Rekonstruksi otomatis via Heapify-Down

=== SLIDE 5: STRUKTUR DATA 2 – DYNAMIC STACK (Bobot: 20)
` Headline: Log Manifest Pesawat & Fitur Undo via Linked List
` Poin Utama:
  ` Alasan Pemilihan: Fitur Undo mutlak membutuhkan sifat LIFO (Last In First Out). Barang         terakhir yang masuk pesawat harus menjadi yang pertama dikeluarkan jika batal.
  
  ` Implementasi: Menggunakan Singly Linked List (Generic) untuk menjamin fleksibilitas ukuran memori secara dinamis
  
  ` Kompleksitas Komputasi (Time Complexity):
  - Push (Simpan Riwayat): O(1) -> Penambahan node di pointer top instan.
  - Pop (Undo Eksekusi): O(1) -> Pengambilan data langsung dari kepala stack

=== SLIDE 6: FUNGSI PENDUKUNG – SEARCHING & SORTING (Bobot: 20)
` Headline: Algoritma Pelacakan & Pengurutan Manifes

` Searching Data (Linear Search):
1. Mencari ID Kargo spesifik di dalam struktur tabel manifes gudang bandara.
2. Kompleksitas: O(n) (Worst Case jika data di ujung atau tidak ada).

` Sorting Data (Selection Sort):
1. Mengurutkan visualisasi kargo berdasarkan kolom berat (Ascending/Descending).
2. Kompleksitas: 0(n2) akibat proses nested loop saat mencari nilai minimum/maksimum berat.

=== SLIDE 7: DEMO APLIKASI & KESIMPULAN
Headline: CargoFlow System Demonstration

Integrasi Priority Queue Heap dan Dynamic Stack sukses menciptakan sistem logistik bandara yang aman dari false overflow dan hemat memori.

Q&A Session / Terima Kasih
