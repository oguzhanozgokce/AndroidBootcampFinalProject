package com.oguzhanozgokce.androidbootcampfinalproject.common

enum class FirebaseErrorCode(val code: String, val message: String) {
    // Network Errors
    NETWORK_ERROR("network-request-failed", "İnternet bağlantınızı kontrol edin"),
    TIMEOUT("timeout", "İşlem zaman aşımına uğradı"),
    UNAVAILABLE("unavailable", "Servis şu anda kullanılamıyor"),

    // Permission Errors
    PERMISSION_DENIED("permission-denied", "Bu işlem için yetkiniz bulunmuyor"),
    UNAUTHENTICATED("unauthenticated", "Oturum açmanız gerekiyor"),

    // Firebase Authentication Errors
    AUTH_INVALID_EMAIL("invalid-email", "Geçersiz e-posta adresi"),
    AUTH_USER_DISABLED("user-disabled", "Kullanıcı hesabı devre dışı bırakılmış"),
    AUTH_USER_NOT_FOUND("user-not-found", "Kullanıcı bulunamadı"),
    AUTH_WRONG_PASSWORD("wrong-password", "Hatalı şifre"),
    AUTH_EMAIL_ALREADY_IN_USE("email-already-in-use", "Bu e-posta adresi zaten kullanımda"),
    AUTH_WEAK_PASSWORD("weak-password", "Şifre çok zayıf (en az 6 karakter olmalı)"),
    AUTH_OPERATION_NOT_ALLOWED("operation-not-allowed", "Bu işlem izin verilmiyor"),
    AUTH_TOO_MANY_REQUESTS("too-many-requests", "Çok fazla deneme yapıldı, lütfen daha sonra tekrar deneyin"),
    AUTH_USER_TOKEN_EXPIRED("user-token-expired", "Oturum süresi dolmuş, lütfen tekrar giriş yapın"),
    AUTH_INVALID_USER_TOKEN("invalid-user-token", "Geçersiz kullanıcı token'ı"),
    AUTH_REQUIRES_RECENT_LOGIN("requires-recent-login", "Bu işlem için yakın zamanda giriş yapmış olmanız gerekiyor"),
    AUTH_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL(
        "account-exists-with-different-credential",
        "Bu e-posta farklı bir giriş yöntemi ile kullanılıyor"
    ),
    AUTH_INVALID_CREDENTIAL("invalid-credential", "Geçersiz kimlik bilgileri"),
    AUTH_CREDENTIAL_ALREADY_IN_USE(
        "credential-already-in-use",
        "Bu kimlik bilgileri zaten başka bir hesapla bağlantılı"
    ),
    AUTH_INVALID_VERIFICATION_CODE("invalid-verification-code", "Geçersiz doğrulama kodu"),
    AUTH_INVALID_VERIFICATION_ID("invalid-verification-id", "Geçersiz doğrulama ID'si"),
    AUTH_EXPIRED_ACTION_CODE("expired-action-code", "İşlem kodu süresi dolmuş"),
    AUTH_INVALID_ACTION_CODE("invalid-action-code", "Geçersiz işlem kodu"),
    AUTH_MISSING_EMAIL("missing-email", "E-posta adresi gerekli"),
    AUTH_MISSING_PASSWORD("missing-password", "Şifre gerekli"),

    // Data Errors
    NOT_FOUND("not-found", "İstenen veri bulunamadı"),
    ALREADY_EXISTS("already-exists", "Bu veri zaten mevcut"),
    FAILED_PRECONDITION("failed-precondition", "İşlem koşulları sağlanamadı"),
    OUT_OF_RANGE("out-of-range", "Veri aralık dışında"),
    DATA_LOSS("data-loss", "Veri kaybı oluştu"),

    // Resource Errors
    RESOURCE_EXHAUSTED("resource-exhausted", "Kaynak limiti aşıldı"),
    QUOTA_EXCEEDED("quota-exceeded", "Kullanım kotası aşıldı"),

    // Internal Errors
    INTERNAL("internal", "Sunucu hatası oluştu"),
    UNKNOWN("unknown", "Bilinmeyen bir hata oluştu"),
    INVALID_ARGUMENT("invalid-argument", "Geçersiz parametre"),
    CANCELLED("cancelled", "İşlem iptal edildi"),
    DEADLINE_EXCEEDED("deadline-exceeded", "İşlem zaman aşımına uğradı"),

    // Custom App Errors
    GAME_NOT_FOUND("game-not-found", "Oyun bulunamadı"),
    SCORE_SAVE_FAILED("score-save-failed", "Skor kaydedilemedi"),
    SETTINGS_LOAD_FAILED("settings-load-failed", "Ayarlar yüklenemedi"),
    INVALID_GAME_STATE("invalid-game-state", "Geçersiz oyun durumu");

    companion object {
        private fun fromCode(code: String?): FirebaseErrorCode {
            return entries.find { it.code == code } ?: UNKNOWN
        }

        fun getMessageByCode(code: String?): String {
            return fromCode(code).message
        }
    }
}