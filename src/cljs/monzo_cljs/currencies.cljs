(ns monzo-cljs.currencies)

(def currencies {:IQD
                 {:decimal_digits 0
                  :name_plural "Iraqi dinars"
                  :symbol "IQD"
                  :name "Iraqi Dinar"
                  :symbol_native "د.ع.‏"
                  :code "IQD"
                  :rounding 0}
                 :MAD
                 {:decimal_digits 2
                  :name_plural "Moroccan dirhams"
                  :symbol "MAD"
                  :name "Moroccan Dirham"
                  :symbol_native "د.م.‏"
                  :code "MAD"
                  :rounding 0}
                 :CHF
                 {:decimal_digits 2
                  :name_plural "Swiss francs"
                  :symbol "CHF"
                  :name "Swiss Franc"
                  :symbol_native "CHF"
                  :code "CHF"
                  :rounding 0.05}
                 :GNF
                 {:decimal_digits 0
                  :name_plural "Guinean francs"
                  :symbol "FG"
                  :name "Guinean Franc"
                  :symbol_native "FG"
                  :code "GNF"
                  :rounding 0}
                 :DOP
                 {:decimal_digits 2
                  :name_plural "Dominican pesos"
                  :symbol "RD$"
                  :name "Dominican Peso"
                  :symbol_native "RD$"
                  :code "DOP"
                  :rounding 0}
                 :SGD
                 {:decimal_digits 2
                  :name_plural "Singapore dollars"
                  :symbol "S$"
                  :name "Singapore Dollar"
                  :symbol_native "$"
                  :code "SGD"
                  :rounding 0}
                 :KHR
                 {:decimal_digits 2
                  :name_plural "Cambodian riels"
                  :symbol "KHR"
                  :name "Cambodian Riel"
                  :symbol_native "៛"
                  :code "KHR"
                  :rounding 0}
                 :MKD
                 {:decimal_digits 2
                  :name_plural "Macedonian denari"
                  :symbol "MKD"
                  :name "Macedonian Denar"
                  :symbol_native "MKD"
                  :code "MKD"
                  :rounding 0}
                 :GBP
                 {:decimal_digits 2
                  :name_plural "British pounds sterling"
                  :symbol "£"
                  :name "British Pound Sterling"
                  :symbol_native "£"
                  :code "GBP"
                  :rounding 0}
                 :TOP
                 {:decimal_digits 2
                  :name_plural "Tongan paʻanga"
                  :symbol "T$"
                  :name "Tongan Paʻanga"
                  :symbol_native "T$"
                  :code "TOP"
                  :rounding 0}
                 :HNL
                 {:decimal_digits 2
                  :name_plural "Honduran lempiras"
                  :symbol "HNL"
                  :name "Honduran Lempira"
                  :symbol_native "L"
                  :code "HNL"
                  :rounding 0}
                 :KWD
                 {:decimal_digits 3
                  :name_plural "Kuwaiti dinars"
                  :symbol "KD"
                  :name "Kuwaiti Dinar"
                  :symbol_native "د.ك.‏"
                  :code "KWD"
                  :rounding 0}
                 :PAB
                 {:decimal_digits 2
                  :name_plural "Panamanian balboas"
                  :symbol "B/."
                  :name "Panamanian Balboa"
                  :symbol_native "B/."
                  :code "PAB"
                  :rounding 0}
                 :KES
                 {:decimal_digits 2
                  :name_plural "Kenyan shillings"
                  :symbol "Ksh"
                  :name "Kenyan Shilling"
                  :symbol_native "Ksh"
                  :code "KES"
                  :rounding 0}
                 :AMD
                 {:decimal_digits 0
                  :name_plural "Armenian drams"
                  :symbol "AMD"
                  :name "Armenian Dram"
                  :symbol_native "դր."
                  :code "AMD"
                  :rounding 0}
                 :NIO
                 {:decimal_digits 2
                  :name_plural "Nicaraguan córdobas"
                  :symbol "C$"
                  :name "Nicaraguan Córdoba"
                  :symbol_native "C$"
                  :code "NIO"
                  :rounding 0}
                 :PKR
                 {:decimal_digits 0
                  :name_plural "Pakistani rupees"
                  :symbol "PKRs"
                  :name "Pakistani Rupee"
                  :symbol_native "₨"
                  :code "PKR"
                  :rounding 0}
                 :MYR
                 {:decimal_digits 2
                  :name_plural "Malaysian ringgits"
                  :symbol "RM"
                  :name "Malaysian Ringgit"
                  :symbol_native "RM"
                  :code "MYR"
                  :rounding 0}
                 :KZT
                 {:decimal_digits 2
                  :name_plural "Kazakhstani tenges"
                  :symbol "KZT"
                  :name "Kazakhstani Tenge"
                  :symbol_native "тңг."
                  :code "KZT"
                  :rounding 0}
                 :ZMK
                 {:decimal_digits 0
                  :name_plural "Zambian kwachas"
                  :symbol "ZK"
                  :name "Zambian Kwacha"
                  :symbol_native "ZK"
                  :code "ZMK"
                  :rounding 0}
                 :BOB
                 {:decimal_digits 2
                  :name_plural "Bolivian bolivianos"
                  :symbol "Bs"
                  :name "Bolivian Boliviano"
                  :symbol_native "Bs"
                  :code "BOB"
                  :rounding 0}
                 :CRC
                 {:decimal_digits 0
                  :name_plural "Costa Rican colóns"
                  :symbol "₡"
                  :name "Costa Rican Colón"
                  :symbol_native "₡"
                  :code "CRC"
                  :rounding 0}
                 :JOD
                 {:decimal_digits 3
                  :name_plural "Jordanian dinars"
                  :symbol "JD"
                  :name "Jordanian Dinar"
                  :symbol_native "د.أ.‏"
                  :code "JOD"
                  :rounding 0}
                 :ERN
                 {:decimal_digits 2
                  :name_plural "Eritrean nakfas"
                  :symbol "Nfk"
                  :name "Eritrean Nakfa"
                  :symbol_native "Nfk"
                  :code "ERN"
                  :rounding 0}
                 :CZK
                 {:decimal_digits 2
                  :name_plural "Czech Republic korunas"
                  :symbol "Kč"
                  :name "Czech Republic Koruna"
                  :symbol_native "Kč"
                  :code "CZK"
                  :rounding 0}
                 :LVL
                 {:decimal_digits 2
                  :name_plural "Latvian lati"
                  :symbol "Ls"
                  :name "Latvian Lats"
                  :symbol_native "Ls"
                  :code "LVL"
                  :rounding 0}
                 :HKD
                 {:decimal_digits 2
                  :name_plural "Hong Kong dollars"
                  :symbol "HK$"
                  :name "Hong Kong Dollar"
                  :symbol_native "$"
                  :code "HKD"
                  :rounding 0}
                 :LYD
                 {:decimal_digits 3
                  :name_plural "Libyan dinars"
                  :symbol "LD"
                  :name "Libyan Dinar"
                  :symbol_native "د.ل.‏"
                  :code "LYD"
                  :rounding 0}
                 :XAF
                 {:decimal_digits 0
                  :name_plural "CFA francs BEAC"
                  :symbol "FCFA"
                  :name "CFA Franc BEAC"
                  :symbol_native "FCFA"
                  :code "XAF"
                  :rounding 0}
                 :GTQ
                 {:decimal_digits 2
                  :name_plural "Guatemalan quetzals"
                  :symbol "GTQ"
                  :name "Guatemalan Quetzal"
                  :symbol_native "Q"
                  :code "GTQ"
                  :rounding 0}
                 :DJF
                 {:decimal_digits 0
                  :name_plural "Djiboutian francs"
                  :symbol "Fdj"
                  :name "Djiboutian Franc"
                  :symbol_native "Fdj"
                  :code "DJF"
                  :rounding 0}
                 :UAH
                 {:decimal_digits 2
                  :name_plural "Ukrainian hryvnias"
                  :symbol "₴"
                  :name "Ukrainian Hryvnia"
                  :symbol_native "₴"
                  :code "UAH"
                  :rounding 0}
                 :RWF
                 {:decimal_digits 0
                  :name_plural "Rwandan francs"
                  :symbol "RWF"
                  :name "Rwandan Franc"
                  :symbol_native "FR"
                  :code "RWF"
                  :rounding 0}
                 :BWP
                 {:decimal_digits 2
                  :name_plural "Botswanan pulas"
                  :symbol "BWP"
                  :name "Botswanan Pula"
                  :symbol_native "P"
                  :code "BWP"
                  :rounding 0}
                 :CLP
                 {:decimal_digits 0
                  :name_plural "Chilean pesos"
                  :symbol "CL$"
                  :name "Chilean Peso"
                  :symbol_native "$"
                  :code "CLP"
                  :rounding 0}
                 :OMR
                 {:decimal_digits 3
                  :name_plural "Omani rials"
                  :symbol "OMR"
                  :name "Omani Rial"
                  :symbol_native "ر.ع.‏"
                  :code "OMR"
                  :rounding 0}
                 :PLN
                 {:decimal_digits 2
                  :name_plural "Polish zlotys"
                  :symbol "zł"
                  :name "Polish Zloty"
                  :symbol_native "zł"
                  :code "PLN"
                  :rounding 0}
                 :MZN
                 {:decimal_digits 2
                  :name_plural "Mozambican meticals"
                  :symbol "MTn"
                  :name "Mozambican Metical"
                  :symbol_native "MTn"
                  :code "MZN"
                  :rounding 0}
                 :AFN
                 {:decimal_digits 0
                  :name_plural "Afghan Afghanis"
                  :symbol "Af"
                  :name "Afghan Afghani"
                  :symbol_native "؋"
                  :code "AFN"
                  :rounding 0}
                 :PYG
                 {:decimal_digits 0
                  :name_plural "Paraguayan guaranis"
                  :symbol "₲"
                  :name "Paraguayan Guarani"
                  :symbol_native "₲"
                  :code "PYG"
                  :rounding 0}
                 :TRY
                 {:decimal_digits 2
                  :name_plural "Turkish Lira"
                  :symbol "TL"
                  :name "Turkish Lira"
                  :symbol_native "TL"
                  :code "TRY"
                  :rounding 0}
                 :BZD
                 {:decimal_digits 2
                  :name_plural "Belize dollars"
                  :symbol "BZ$"
                  :name "Belize Dollar"
                  :symbol_native "$"
                  :code "BZD"
                  :rounding 0}
                 :MDL
                 {:decimal_digits 2
                  :name_plural "Moldovan lei"
                  :symbol "MDL"
                  :name "Moldovan Leu"
                  :symbol_native "MDL"
                  :code "MDL"
                  :rounding 0}
                 :JPY
                 {:decimal_digits 0
                  :name_plural "Japanese yen"
                  :symbol "¥"
                  :name "Japanese Yen"
                  :symbol_native "￥"
                  :code "JPY"
                  :rounding 0}
                 :INR
                 {:decimal_digits 2
                  :name_plural "Indian rupees"
                  :symbol "Rs"
                  :name "Indian Rupee"
                  :symbol_native "টকা"
                  :code "INR"
                  :rounding 0}
                 :RSD
                 {:decimal_digits 0
                  :name_plural "Serbian dinars"
                  :symbol "din."
                  :name "Serbian Dinar"
                  :symbol_native "дин."
                  :code "RSD"
                  :rounding 0}
                 :TTD
                 {:decimal_digits 2
                  :name_plural "Trinidad and Tobago dollars"
                  :symbol "TT$"
                  :name "Trinidad and Tobago Dollar"
                  :symbol_native "$"
                  :code "TTD"
                  :rounding 0}
                 :BIF
                 {:decimal_digits 0
                  :name_plural "Burundian francs"
                  :symbol "FBu"
                  :name "Burundian Franc"
                  :symbol_native "FBu"
                  :code "BIF"
                  :rounding 0}
                 :SEK
                 {:decimal_digits 2
                  :name_plural "Swedish kronor"
                  :symbol "Skr"
                  :name "Swedish Krona"
                  :symbol_native "kr"
                  :code "SEK"
                  :rounding 0}
                 :IDR
                 {:decimal_digits 0
                  :name_plural "Indonesian rupiahs"
                  :symbol "Rp"
                  :name "Indonesian Rupiah"
                  :symbol_native "Rp"
                  :code "IDR"
                  :rounding 0}
                 :ARS
                 {:decimal_digits 2
                  :name_plural "Argentine pesos"
                  :symbol "AR$"
                  :name "Argentine Peso"
                  :symbol_native "$"
                  :code "ARS"
                  :rounding 0}
                 :VND
                 {:decimal_digits 0
                  :name_plural "Vietnamese dong"
                  :symbol "₫"
                  :name "Vietnamese Dong"
                  :symbol_native "₫"
                  :code "VND"
                  :rounding 0}
                 :MUR
                 {:decimal_digits 0
                  :name_plural "Mauritian rupees"
                  :symbol "MURs"
                  :name "Mauritian Rupee"
                  :symbol_native "MURs"
                  :code "MUR"
                  :rounding 0}
                 :NGN
                 {:decimal_digits 2
                  :name_plural "Nigerian nairas"
                  :symbol "₦"
                  :name "Nigerian Naira"
                  :symbol_native "₦"
                  :code "NGN"
                  :rounding 0}
                 :KRW
                 {:decimal_digits 0
                  :name_plural "South Korean won"
                  :symbol "₩"
                  :name "South Korean Won"
                  :symbol_native "₩"
                  :code "KRW"
                  :rounding 0}
                 :MGA
                 {:decimal_digits 0
                  :name_plural "Malagasy Ariaries"
                  :symbol "MGA"
                  :name "Malagasy Ariary"
                  :symbol_native "MGA"
                  :code "MGA"
                  :rounding 0}
                 :KMF
                 {:decimal_digits 0
                  :name_plural "Comorian francs"
                  :symbol "CF"
                  :name "Comorian Franc"
                  :symbol_native "FC"
                  :code "KMF"
                  :rounding 0}
                 :AED
                 {:decimal_digits 2
                  :name_plural "UAE dirhams"
                  :symbol "AED"
                  :name "United Arab Emirates Dirham"
                  :symbol_native "د.إ.‏"
                  :code "AED"
                  :rounding 0}
                 :EGP
                 {:decimal_digits 2
                  :name_plural "Egyptian pounds"
                  :symbol "EGP"
                  :name "Egyptian Pound"
                  :symbol_native "ج.م.‏"
                  :code "EGP"
                  :rounding 0}
                 :THB
                 {:decimal_digits 2
                  :name_plural "Thai baht"
                  :symbol "฿"
                  :name "Thai Baht"
                  :symbol_native "฿"
                  :code "THB"
                  :rounding 0}
                 :DZD
                 {:decimal_digits 2
                  :name_plural "Algerian dinars"
                  :symbol "DA"
                  :name "Algerian Dinar"
                  :symbol_native "د.ج.‏"
                  :code "DZD"
                  :rounding 0}
                 :TZS
                 {:decimal_digits 0
                  :name_plural "Tanzanian shillings"
                  :symbol "TSh"
                  :name "Tanzanian Shilling"
                  :symbol_native "TSh"
                  :code "TZS"
                  :rounding 0}
                 :LKR
                 {:decimal_digits 2
                  :name_plural "Sri Lankan rupees"
                  :symbol "SLRs"
                  :name "Sri Lankan Rupee"
                  :symbol_native "SL Re"
                  :code "LKR"
                  :rounding 0}
                 :YER
                 {:decimal_digits 0
                  :name_plural "Yemeni rials"
                  :symbol "YR"
                  :name "Yemeni Rial"
                  :symbol_native "ر.ي.‏"
                  :code "YER"
                  :rounding 0}
                 :NZD
                 {:decimal_digits 2
                  :name_plural "New Zealand dollars"
                  :symbol "NZ$"
                  :name "New Zealand Dollar"
                  :symbol_native "$"
                  :code "NZD"
                  :rounding 0}
                 :USD
                 {:decimal_digits 2
                  :name_plural "US dollars"
                  :symbol "$"
                  :name "US Dollar"
                  :symbol_native "$"
                  :code "USD"
                  :rounding 0}
                 :UGX
                 {:decimal_digits 0
                  :name_plural "Ugandan shillings"
                  :symbol "USh"
                  :name "Ugandan Shilling"
                  :symbol_native "USh"
                  :code "UGX"
                  :rounding 0}
                 :TWD
                 {:decimal_digits 2
                  :name_plural "New Taiwan dollars"
                  :symbol "NT$"
                  :name "New Taiwan Dollar"
                  :symbol_native "NT$"
                  :code "TWD"
                  :rounding 0}
                 :CAD
                 {:decimal_digits 2
                  :name_plural "Canadian dollars"
                  :symbol "CA$"
                  :name "Canadian Dollar"
                  :symbol_native "$"
                  :code "CAD"
                  :rounding 0}
                 :ILS
                 {:decimal_digits 2
                  :name_plural "Israeli new sheqels"
                  :symbol "₪"
                  :name "Israeli New Sheqel"
                  :symbol_native "₪"
                  :code "ILS"
                  :rounding 0}
                 :MMK
                 {:decimal_digits 0
                  :name_plural "Myanma kyats"
                  :symbol "MMK"
                  :name "Myanma Kyat"
                  :symbol_native "K"
                  :code "MMK"
                  :rounding 0}
                 :CNY
                 {:decimal_digits 2
                  :name_plural "Chinese yuan"
                  :symbol "CN¥"
                  :name "Chinese Yuan"
                  :symbol_native "CN¥"
                  :code "CNY"
                  :rounding 0}
                 :MXN
                 {:decimal_digits 2
                  :name_plural "Mexican pesos"
                  :symbol "MX$"
                  :name "Mexican Peso"
                  :symbol_native "$"
                  :code "MXN"
                  :rounding 0}
                 :PEN
                 {:decimal_digits 2
                  :name_plural "Peruvian nuevos soles"
                  :symbol "S/."
                  :name "Peruvian Nuevo Sol"
                  :symbol_native "S/."
                  :code "PEN"
                  :rounding 0}
                 :IRR
                 {:decimal_digits 0
                  :name_plural "Iranian rials"
                  :symbol "IRR"
                  :name "Iranian Rial"
                  :symbol_native "﷼"
                  :code "IRR"
                  :rounding 0}
                 :CDF
                 {:decimal_digits 2
                  :name_plural "Congolese francs"
                  :symbol "CDF"
                  :name "Congolese Franc"
                  :symbol_native "FrCD"
                  :code "CDF"
                  :rounding 0}
                 :GHS
                 {:decimal_digits 2
                  :name_plural "Ghanaian cedis"
                  :symbol "GH₵"
                  :name "Ghanaian Cedi"
                  :symbol_native "GH₵"
                  :code "GHS"
                  :rounding 0}
                 :SYP
                 {:decimal_digits 0
                  :name_plural "Syrian pounds"
                  :symbol "SY£"
                  :name "Syrian Pound"
                  :symbol_native "ل.س.‏"
                  :code "SYP"
                  :rounding 0}
                 :SOS
                 {:decimal_digits 0
                  :name_plural "Somali shillings"
                  :symbol "Ssh"
                  :name "Somali Shilling"
                  :symbol_native "Ssh"
                  :code "SOS"
                  :rounding 0}
                 :BDT
                 {:decimal_digits 2
                  :name_plural "Bangladeshi takas"
                  :symbol "Tk"
                  :name "Bangladeshi Taka"
                  :symbol_native "৳"
                  :code "BDT"
                  :rounding 0}
                 :EUR
                 {:decimal_digits 2
                  :name_plural "euros"
                  :symbol "€"
                  :name "Euro"
                  :symbol_native "€"
                  :code "EUR"
                  :rounding 0}
                 :RUB
                 {:decimal_digits 2
                  :name_plural "Russian rubles"
                  :symbol "RUB"
                  :name "Russian Ruble"
                  :symbol_native "руб."
                  :code "RUB"
                  :rounding 0}
                 :UZS
                 {:decimal_digits 0
                  :name_plural "Uzbekistan som"
                  :symbol "UZS"
                  :name "Uzbekistan Som"
                  :symbol_native "UZS"
                  :code "UZS"
                  :rounding 0}
                 :RON
                 {:decimal_digits 2
                  :name_plural "Romanian lei"
                  :symbol "RON"
                  :name "Romanian Leu"
                  :symbol_native "RON"
                  :code "RON"
                  :rounding 0}
                 :ALL
                 {:decimal_digits 0
                  :name_plural "Albanian lekë"
                  :symbol "ALL"
                  :name "Albanian Lek"
                  :symbol_native "Lek"
                  :code "ALL"
                  :rounding 0}
                 :NAD
                 {:decimal_digits 2
                  :name_plural "Namibian dollars"
                  :symbol "N$"
                  :name "Namibian Dollar"
                  :symbol_native "N$"
                  :code "NAD"
                  :rounding 0}
                 :NOK
                 {:decimal_digits 2
                  :name_plural "Norwegian kroner"
                  :symbol "Nkr"
                  :name "Norwegian Krone"
                  :symbol_native "kr"
                  :code "NOK"
                  :rounding 0}
                 :NPR
                 {:decimal_digits 2
                  :name_plural "Nepalese rupees"
                  :symbol "NPRs"
                  :name "Nepalese Rupee"
                  :symbol_native "नेरू"
                  :code "NPR"
                  :rounding 0}
                 :LBP
                 {:decimal_digits 0
                  :name_plural "Lebanese pounds"
                  :symbol "LB£"
                  :name "Lebanese Pound"
                  :symbol_native "ل.ل.‏"
                  :code "LBP"
                  :rounding 0}
                 :SDG
                 {:decimal_digits 2
                  :name_plural "Sudanese pounds"
                  :symbol "SDG"
                  :name "Sudanese Pound"
                  :symbol_native "SDG"
                  :code "SDG"
                  :rounding 0}
                 :ISK
                 {:decimal_digits 0
                  :name_plural "Icelandic krónur"
                  :symbol "Ikr"
                  :name "Icelandic Króna"
                  :symbol_native "kr"
                  :code "ISK"
                  :rounding 0}
                 :BHD
                 {:decimal_digits 3
                  :name_plural "Bahraini dinars"
                  :symbol "BD"
                  :name "Bahraini Dinar"
                  :symbol_native "د.ب.‏"
                  :code "BHD"
                  :rounding 0}
                 :HRK
                 {:decimal_digits 2
                  :name_plural "Croatian kunas"
                  :symbol "kn"
                  :name "Croatian Kuna"
                  :symbol_native "kn"
                  :code "HRK"
                  :rounding 0}
                 :GEL
                 {:decimal_digits 2
                  :name_plural "Georgian laris"
                  :symbol "GEL"
                  :name "Georgian Lari"
                  :symbol_native "GEL"
                  :code "GEL"
                  :rounding 0}
                 :MOP
                 {:decimal_digits 2
                  :name_plural "Macanese patacas"
                  :symbol "MOP$"
                  :name "Macanese Pataca"
                  :symbol_native "MOP$"
                  :code "MOP"
                  :rounding 0}
                 :PHP
                 {:decimal_digits 2
                  :name_plural "Philippine pesos"
                  :symbol "₱"
                  :name "Philippine Peso"
                  :symbol_native "₱"
                  :code "PHP"
                  :rounding 0}
                 :BND
                 {:decimal_digits 2
                  :name_plural "Brunei dollars"
                  :symbol "BN$"
                  :name "Brunei Dollar"
                  :symbol_native "$"
                  :code "BND"
                  :rounding 0}
                 :HUF
                 {:decimal_digits 0
                  :name_plural "Hungarian forints"
                  :symbol "Ft"
                  :name "Hungarian Forint"
                  :symbol_native "Ft"
                  :code "HUF"
                  :rounding 0}
                 :TND
                 {:decimal_digits 3
                  :name_plural "Tunisian dinars"
                  :symbol "DT"
                  :name "Tunisian Dinar"
                  :symbol_native "د.ت.‏"
                  :code "TND"
                  :rounding 0}
                 :LTL
                 {:decimal_digits 2
                  :name_plural "Lithuanian litai"
                  :symbol "Lt"
                  :name "Lithuanian Litas"
                  :symbol_native "Lt"
                  :code "LTL"
                  :rounding 0}
                 :SAR
                 {:decimal_digits 2
                  :name_plural "Saudi riyals"
                  :symbol "SR"
                  :name "Saudi Riyal"
                  :symbol_native "ر.س.‏"
                  :code "SAR"
                  :rounding 0}
                 :COP
                 {:decimal_digits 0
                  :name_plural "Colombian pesos"
                  :symbol "CO$"
                  :name "Colombian Peso"
                  :symbol_native "$"
                  :code "COP"
                  :rounding 0}
                 :UYU
                 {:decimal_digits 2
                  :name_plural "Uruguayan pesos"
                  :symbol "$U"
                  :name "Uruguayan Peso"
                  :symbol_native "$"
                  :code "UYU"
                  :rounding 0}
                 :CVE
                 {:decimal_digits 2
                  :name_plural "Cape Verdean escudos"
                  :symbol "CV$"
                  :name "Cape Verdean Escudo"
                  :symbol_native "CV$"
                  :code "CVE"
                  :rounding 0}
                 :BAM
                 {:decimal_digits 2
                  :name_plural "Bosnia-Herzegovina convertible marks"
                  :symbol "KM"
                  :name "Bosnia-Herzegovina Convertible Mark"
                  :symbol_native "KM"
                  :code "BAM"
                  :rounding 0}
                 :AZN
                 {:decimal_digits 2
                  :name_plural "Azerbaijani manats"
                  :symbol "man."
                  :name "Azerbaijani Manat"
                  :symbol_native "ман."
                  :code "AZN"
                  :rounding 0}
                 :AUD
                 {:decimal_digits 2
                  :name_plural "Australian dollars"
                  :symbol "AU$"
                  :name "Australian Dollar"
                  :symbol_native "$"
                  :code "AUD"
                  :rounding 0}
                 :BRL
                 {:decimal_digits 2
                  :name_plural "Brazilian reals"
                  :symbol "R$"
                  :name "Brazilian Real"
                  :symbol_native "R$"
                  :code "BRL"
                  :rounding 0}
                 :BYR
                 {:decimal_digits 0
                  :name_plural "Belarusian rubles"
                  :symbol "BYR"
                  :name "Belarusian Ruble"
                  :symbol_native "BYR"
                  :code "BYR"
                  :rounding 0}
                 :JMD
                 {:decimal_digits 2
                  :name_plural "Jamaican dollars"
                  :symbol "J$"
                  :name "Jamaican Dollar"
                  :symbol_native "$"
                  :code "JMD"
                  :rounding 0}
                 :DKK
                 {:decimal_digits 2
                  :name_plural "Danish kroner"
                  :symbol "Dkr"
                  :name "Danish Krone"
                  :symbol_native "kr"
                  :code "DKK"
                  :rounding 0}
                 :ETB
                 {:decimal_digits 2
                  :name_plural "Ethiopian birrs"
                  :symbol "Br"
                  :name "Ethiopian Birr"
                  :symbol_native "Br"
                  :code "ETB"
                  :rounding 0}
                 :QAR
                 {:decimal_digits 2
                  :name_plural "Qatari rials"
                  :symbol "QR"
                  :name "Qatari Rial"
                  :symbol_native "ر.ق.‏"
                  :code "QAR"
                  :rounding 0}
                 :ZAR
                 {:decimal_digits 2
                  :name_plural "South African rand"
                  :symbol "R"
                  :name "South African Rand"
                  :symbol_native "R"
                  :code "ZAR"
                  :rounding 0}
                 :VEF
                 {:decimal_digits 2
                  :name_plural "Venezuelan bolívars"
                  :symbol "Bs.F."
                  :name "Venezuelan Bolívar"
                  :symbol_native "Bs.F."
                  :code "VEF"
                  :rounding 0}
                 :BGN
                 {:decimal_digits 2
                  :name_plural "Bulgarian leva"
                  :symbol "BGN"
                  :name "Bulgarian Lev"
                  :symbol_native "лв."
                  :code "BGN"
                  :rounding 0}
                 :EEK
                 {:decimal_digits 2
                  :name_plural "Estonian kroons"
                  :symbol "Ekr"
                  :name "Estonian Kroon"
                  :symbol_native "kr"
                  :code "EEK"
                  :rounding 0}
                 :XOF
                 {:decimal_digits 0
                  :name_plural "CFA francs BCEAO"
                  :symbol "CFA"
                  :name "CFA Franc BCEAO"
                  :symbol_native "CFA"
                  :code "XOF"
                  :rounding 0}})
