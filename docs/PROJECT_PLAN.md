# fintech-wallet: Proje Planı

## Proje nedir?

GetirFinans, Papara, Tosla benzeri bir **dijital cüzdan** platformunun backend'i.
Kullanıcılar web ve mobilden hesap açar, kimliğini doğrular, cüzdanına para yükler,
başkasına para gönderir ve döviz alıp satar. Şirket tarafında bir yönetim paneli vardır.

Amaç: CV'ye eklenebilecek, kurumsal standartlarda, ileride canlıda çalışacak bir Spring Boot projesi
geliştirmek ve bu sırada sektörde kullanılan önemli teknikleri gerçekten uygulayarak öğrenmek.

### Kullanıcı akışı

1. Üyelik (e-posta + şifre)
2. Kimlik doğrulama (KYC): başvuru → admin incelemesi → onay/red
3. Cüzdan açılışı: TRY, isteğe bağlı USD/EUR
4. Para yükleme (simüle edilmiş banka/ödeme sağlayıcı)
5. Kullanıcılar arası para transferi
6. Bildirim (e-posta/push)
7. Döviz alım-satım (dış kur API'si)
8. İşlem geçmişi

Kapsam dışı: fiziksel/sanal kart, gerçek banka/FAST entegrasyonu, fatura ödeme, kredi, yatırım.

## Mimari kararlar

- **Modüler monolit**, package-by-feature. Modül içinde `api / application / domain / infrastructure`.
- Modül sınırları Spring Modulith + ArchUnit ile testlerle korunur. Mikroservis yok (bilinçli karar).
- Backend hem web hem mobil istemcilere hizmet verir. Frontend backend bittikten sonra yapılacak.
- Önce **lokalde** (Docker Compose) çalışılacak; canlıya sonra VPS üzerinde çıkılacak, en son Kubernetes (k3s).

## Stack

Java 25 (LTS), Spring Boot 4, PostgreSQL, Flyway, Spring Data JPA, Spring Security + OAuth2/JWT (Keycloak),
Kafka, Redis, Resilience4j, MapStruct, Bean Validation, OpenAPI, Testcontainers, ArchUnit,
Micrometer + OpenTelemetry + Prometheus + Grafana, Docker, GitHub Actions.

## Modüller

| Modül | Sorumluluk |
|---|---|
| identity | Kayıt, giriş, roller (müşteri/admin) |
| customer | Müşteri bilgileri, KYC süreci (state machine) |
| account | TRY/USD/EUR cüzdanları, bakiye |
| ledger | Double-entry muhasebe defteri |
| transfer | Transfer, para yükleme/çekme |
| fx | Döviz kurları, döviz çevirme |
| notification | E-posta/push bildirimleri (event ile) |
| audit | İşlem/denetim kayıtları |

## Öğrenme kuralı

Her önemli teknik **1-2 yerde** kullanılır, gerçek projedeki gibi her yere kopyalanmaz.

| Teknik | Nerede |
|---|---|
| Idempotency key | Transfer, para yükleme |
| Pessimistic / optimistic locking | Bakiye güncelleme / müşteri profili |
| Double-entry ledger | ledger |
| State machine | KYC, transfer durumları |
| Transactional outbox + Kafka | transfer → notification, audit |
| Domain events (Spring Modulith) | Modüller arası iletişim |
| Resilience4j (circuit breaker, retry) | fx API, sahte ödeme sağlayıcı |
| Redis cache | Döviz kurları |
| Rate limiting | Login, transfer |
| Scheduled job + ShedLock | Günlük ledger mutabakatı |
| KVKK / PII maskeleme | Loglarda TCKN, IBAN, telefon |
| BigDecimal / Money tipi | Tüm para işlemleri |
| Flyway | Tüm şema |
| Testcontainers + ArchUnit | Integration ve mimari testler |
| Gözlemlenebilirlik | Tüm sistem |

## Yol haritası

- [x] 0. Ortam kontrolü: Java, Docker, Git, IDE
- [ ] 1. İskelet: proje yapısı, PostgreSQL, Flyway, Docker Compose, global hata yönetimi, CI
- [ ] 2. identity + customer: auth, KYC state machine
- [ ] 3. account + ledger: bakiye, double-entry, locking
- [ ] 4. transfer: idempotency, transaction yönetimi
- [ ] 5. Event'ler: outbox, Kafka, notification, audit
- [ ] 6. fx: dış entegrasyon, Resilience4j, Redis
- [ ] 7. Sağlamlaştırma: rate limiting, PII maskeleme, ShedLock mutabakat job'ı
- [ ] 8. Gözlemlenebilirlik + VPS'e deploy
- [ ] 9. (Bonus) Kubernetes (k3s + Helm), ardından frontend
