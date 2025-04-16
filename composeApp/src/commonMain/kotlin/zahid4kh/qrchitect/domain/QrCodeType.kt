package zahid4kh.qrchitect.domain

enum class QrCodeType {
    TEXT,
    URL,
    EMAIL,
    PHONE,
    SMS,
    WIFI,
    CONTACT,
    CALENDAR,
    LOCATION,
    PAYMENT,
    MENU,
    BUSINESS_CARD;

    fun getDisplayName(): String {
        return when (this) {
            TEXT -> "Text"
            URL -> "URL/Website"
            EMAIL -> "Email"
            PHONE -> "Phone Number"
            SMS -> "SMS Message"
            WIFI -> "Wi-Fi Network"
            CONTACT -> "Contact Information"
            CALENDAR -> "Calendar Event"
            LOCATION -> "Geographic Location"
            PAYMENT -> "Payment Information"
            MENU -> "Restaurant Menu"
            BUSINESS_CARD -> "Business Card"
        }
    }

    fun getPlaceholderContent(): String {
        return when (this) {
            TEXT -> "Enter your text here"
            URL -> "https://github.com"
            EMAIL -> "mailto:halilzahid@gmail.com?subject=Subject&body=Message"
            PHONE -> "tel:+4917673550562"
            SMS -> "smsto:+4917673550562:Your message here"
            WIFI -> "WIFI:S:NetworkName;T:WPA;P:Password;;"
            CONTACT -> "BEGIN:VCARD\nVERSION:3.0\nN:Lastname;Firstname\nTEL:+1234567890\nEMAIL:email@example.com\nEND:VCARD"
            CALENDAR -> "BEGIN:VEVENT\nSUMMARY:Event Title\nLOCATION:Event Location\nDTSTART:20250417T090000Z\nDTEND:20250417T100000Z\nEND:VEVENT"
            LOCATION -> "geo:37.786971,-122.399677"
            PAYMENT -> "bitcoin:175tWpb8K1S7NmH4Zx6rewF9WQrcZv245W?amount=0.01"
            MENU -> "MENU:Restaurant Name;Item1:Price1;Item2:Price2;Item3:Price3;"
            BUSINESS_CARD -> "BEGIN:VCARD\nVERSION:3.0\nN:Lastname;Firstname\nORG:Company\nTITLE:Job Title\nTEL:+1234567890\nEMAIL:email@example.com\nADR:;;Street;City;State;ZIP;Country\nURL:https://example.com\nEND:VCARD"
        }
    }
}