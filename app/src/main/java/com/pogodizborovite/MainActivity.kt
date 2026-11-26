package com.pogodizborovite
import android.os.Bundle
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import android.media.AudioManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.media.ToneGenerator
import org.json.JSONObject
import org.json.JSONArray
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.Job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


private fun uiText(key:String,lang:String):String {
 val rows=mapOf(
  "mk" to listOf("ИЗБЕРИ КАТЕГОРИЈА","← КАТЕГОРИИ","КОЈ ЗБОР Е ОВА?","БРОЈ НА БУКВИ: ","Одговор","ПРОВЕРИ","💡 ПОМОШ","⚙ ПАРАМЕТРИ","ГОТОВО","ИЗЛЕЗ","ОТКАЖИ","ВКУПНО ПОЕНИ","Погодувај, учи и забавувај се!"),
  "fr" to listOf("CHOISIR UNE CATÉGORIE","← CATÉGORIES","QUEL EST CE MOT ?","NOMBRE DE LETTRES : ","Réponse","VÉRIFIER","💡 INDICE","⚙ PARAMÈTRES","TERMINÉ","QUITTER","ANNULER","TOTAL DES POINTS","Devinez, apprenez et amusez-vous !"),
  "sq" to listOf("ZGJIDH KATEGORINË","← KATEGORITË","CILA ËSHTË KJO FJALË?","NUMRI I SHKRONJAVE: ","Përgjigjja","KONTROLLO","💡 NDIHMË","⚙ CILËSIMET","U KRYE","DIL","ANULO","PIKËT TOTALE","Gjej, mëso dhe argëtohu!"),
  "en" to listOf("CHOOSE A CATEGORY","← CATEGORIES","WHAT IS THIS WORD?","NUMBER OF LETTERS: ","Answer","CHECK","💡 HINT","⚙ SETTINGS","DONE","EXIT","CANCEL","TOTAL POINTS","Guess, learn and have fun!"),
  "de" to listOf("KATEGORIE WÄHLEN","← KATEGORIEN","WELCHES WORT IST DAS?","ANZAHL DER BUCHSTABEN: ","Antwort","PRÜFEN","💡 HINWEIS","⚙ EINSTELLUNGEN","FERTIG","BEENDEN","ABBRECHEN","PUNKTE GESAMT","Rate, lerne und hab Spaß!"),
  "it" to listOf("SCEGLI UNA CATEGORIA","← CATEGORIE","CHE PAROLA È QUESTA?","NUMERO DI LETTERE: ","Risposta","VERIFICA","💡 AIUTO","⚙ IMPOSTAZIONI","FATTO","ESCI","ANNULLA","PUNTI TOTALI","Indovina, impara e divertiti!"),
  "es" to listOf("ELIGE UNA CATEGORÍA","← CATEGORÍAS","¿QUÉ PALABRA ES ESTA?","NÚMERO DE LETRAS: ","Respuesta","COMPROBAR","💡 PISTA","⚙ AJUSTES","LISTO","SALIR","CANCELAR","PUNTOS TOTALES","¡Adivina, aprende y diviértete!"),
  "tr" to listOf("KATEGORİ SEÇ","← KATEGORİLER","BU HANGİ KELİME?","HARF SAYISI: ","Cevap","KONTROL ET","💡 İPUCU","⚙ AYARLAR","TAMAM","ÇIKIŞ","İPTAL","TOPLAM PUAN","Tahmin et, öğren ve eğlen!"),
  "bs" to listOf("IZABERI KATEGORIJU","← KATEGORIJE","KOJA JE OVO RIJEČ?","BROJ SLOVA: ","Odgovor","PROVJERI","💡 POMOĆ","⚙ POSTAVKE","GOTOVO","IZLAZ","OTKAŽI","UKUPNO BODOVA","Pogađaj, uči i zabavi se!"),
  "sr" to listOf("ИЗАБЕРИ КАТЕГОРИЈУ","← КАТЕГОРИЈЕ","КОЈА ЈЕ ОВО РЕЧ?","БРОЈ СЛОВА: ","Одговор","ПРОВЕРИ","💡 ПОМОЋ","⚙ ПОДЕШАВАЊА","ГОТОВО","ИЗЛАЗ","ОТКАЖИ","УКУПНО ПОЕНА","Погађај, учи и забави се!"),
  "ar" to listOf("اختر فئة","← الفئات","ما هذه الكلمة؟","عدد الحروف: ","الإجابة","تحقق","💡 تلميح","⚙ الإعدادات","تم","خروج","إلغاء","مجموع النقاط","خمن وتعلم واستمتع!"),
  "ru" to listOf("ВЫБЕРИТЕ КАТЕГОРИЮ","← КАТЕГОРИИ","ЧТО ЭТО ЗА СЛОВО?","КОЛИЧЕСТВО БУКВ: ","Ответ","ПРОВЕРИТЬ","💡 ПОДСКАЗКА","⚙ НАСТРОЙКИ","ГОТОВО","ВЫХОД","ОТМЕНА","ВСЕГО ОЧКОВ","Угадывай, учись и развлекайся!"),
  "el" to listOf("ΕΠΙΛΕΞΤΕ ΚΑΤΗΓΟΡΙΑ","← ΚΑΤΗΓΟΡΙΕΣ","ΠΟΙΑ ΕΙΝΑΙ ΑΥΤΗ Η ΛΕΞΗ;","ΑΡΙΘΜΟΣ ΓΡΑΜΜΑΤΩΝ: ","Απάντηση","ΕΛΕΓΧΟΣ","💡 ΥΠΟΔΕΙΞΗ","⚙ ΡΥΘΜΙΣΕΙΣ","ΕΤΟΙΜΟ","ΕΞΟΔΟΣ","ΑΚΥΡΩΣΗ","ΣΥΝΟΛΟ ΠΟΝΤΩΝ","Μάντεψε, μάθε και διασκέδασε!"),
  "pt" to listOf("ESCOLHE UMA CATEGORIA","← CATEGORIAS","QUE PALAVRA É ESTA?","NÚMERO DE LETRAS: ","Resposta","VERIFICAR","💡 DICA","⚙ DEFINIÇÕES","CONCLUÍDO","SAIR","CANCELAR","PONTOS TOTAIS","Adivinha, aprende e diverte-te!"),
  "nl" to listOf("KIES EEN CATEGORIE","← CATEGORIEËN","WELK WOORD IS DIT?","AANTAL LETTERS: ","Antwoord","CONTROLEREN","💡 HINT","⚙ INSTELLINGEN","KLAAR","AFSLUITEN","ANNULEREN","TOTAAL AANTAL PUNTEN","Raad, leer en heb plezier!"),
  "pl" to listOf("WYBIERZ KATEGORIĘ","← KATEGORIE","CO TO ZA SŁOWO?","LICZBA LITER: ","Odpowiedź","SPRAWDŹ","💡 PODPOWIEDŹ","⚙ USTAWIENIA","GOTOWE","WYJŚCIE","ANULUJ","ŁĄCZNA LICZBA PUNKTÓW","Zgaduj, ucz się i baw się dobrze!"),
  "cs" to listOf("VYBERTE KATEGORII","← KATEGORIE","CO JE TO ZA SLOVO?","POČET PÍSMEN: ","Odpověď","ZKONTROLOVAT","💡 NÁPOVĚDA","⚙ NASTAVENÍ","HOTOVO","KONEC","ZRUŠIT","CELKEM BODŮ","Hádej, uč se a bav se!"),
  "sk" to listOf("VYBERTE KATEGÓRIU","← KATEGÓRIE","AKÉ JE TO SLOVO?","POČET PÍSMEN: ","Odpoveď","SKONTROLOVAŤ","💡 NÁPOVEDA","⚙ NASTAVENIA","HOTOVO","KONIEC","ZRUŠIŤ","CELKOVÝ POČET BODOV","Hádaj, uč sa a zabávaj sa!"),
  "hr" to listOf("ODABERI KATEGORIJU","← KATEGORIJE","KOJA JE OVO RIJEČ?","BROJ SLOVA: ","Odgovor","PROVJERI","💡 POMOĆ","⚙ POSTAVKE","GOTOVO","IZLAZ","ODUSTANI","UKUPNO BODOVA","Pogađaj, uči i zabavi se!"),
  "sl" to listOf("IZBERI KATEGORIJO","← KATEGORIJE","KATERA BESEDA JE TO?","ŠTEVILO ČRK: ","Odgovor","PREVERI","💡 NAMIG","⚙ NASTAVITVE","KONČANO","IZHOD","PREKLIČI","SKUPAJ TOČK","Ugibaj, uči se in se zabavaj!")
 )
 val keys=listOf("choose_category","back_categories","what_word","letter_count","answer","check","hint","settings","done","exit","cancel","total_points","tagline")
 val i=keys.indexOf(key)
 return if(i<0) key else (rows[lang] ?: rows["mk"]!!).getOrElse(i){key}
}



private fun extraText(key:String,lang:String):String {
 val en=mapOf(
  "words_photos" to "words with pictures","level" to "LEVEL","update" to "UPDATE","checking" to "Checking for updates…",
  "no_release" to "No version published yet.","latest" to "You already have the latest version","found" to "New version found. Downloading…",
  "failed" to "Unable to check. Check your internet connection and try again.","correct" to "BRAVO! CORRECT ANSWER! 🎉",
  "ten_correct" to "🎵 correct answers! TIME FOR MUSIC!","wrong" to "Not correct. Try again!","wrong_final" to "3 WRONG ATTEMPTS! -2 POINTS. Next question…",
  "first_letter" to "FIRST LETTER: ","next_level" to "THE NEXT LEVEL OPENS AUTOMATICALLY…","dark_light" to "Dark theme / Light theme",
  "background_music" to "Background music","game_sounds" to "Game sounds","music_volume" to "Music volume: ",
  "effects_volume" to "Sound effects volume: ","volume_help" to "You can adjust the music and sound effects separately.",
  "exit_title" to "Exit the game?","exit_confirm" to "Are you sure you want to close the app?","enter_word" to "Enter the correct word!"
 )
 val fr=mapOf(
  "words_photos" to "mots illustrés","level" to "NIVEAU","update" to "MISE À JOUR","checking" to "Recherche de mises à jour…",
  "no_release" to "Aucune version publiée pour le moment.","latest" to "Tu as déjà la dernière version","found" to "Nouvelle version trouvée. Téléchargement…",
  "failed" to "Vérification impossible. Vérifie ta connexion Internet et réessaie.","correct" to "BRAVO ! BONNE RÉPONSE ! 🎉",
  "ten_correct" to "🎵 bonnes réponses ! PLACE À LA MUSIQUE !","wrong" to "Ce n’est pas correct. Réessaie !","wrong_final" to "3 ERREURS ! -2 POINTS. Question suivante…",
  "first_letter" to "PREMIÈRE LETTRE : ","next_level" to "LE NIVEAU SUIVANT S’OUVRE AUTOMATIQUEMENT…","dark_light" to "Thème sombre / Thème clair",
  "background_music" to "Musique de fond","game_sounds" to "Sons du jeu","music_volume" to "Volume de la musique : ",
  "effects_volume" to "Volume des effets sonores : ","volume_help" to "Tu peux régler séparément la musique et les effets sonores.",
  "exit_title" to "Quitter le jeu ?","exit_confirm" to "Veux-tu vraiment fermer l’application ?","enter_word" to "Trouve le bon mot !"
 )
 val sq=mapOf(
  "words_photos" to "fjalë me fotografi","level" to "NIVELI","update" to "PËRDITËSO","checking" to "Po kontrollohen përditësimet…",
  "no_release" to "Nuk ka ende version të publikuar.","latest" to "Ke tashmë versionin më të fundit","found" to "U gjet version i ri. Po shkarkohet…",
  "failed" to "Kontrolli dështoi. Kontrollo internetin dhe provo përsëri.","correct" to "BRAVO! PËRGJIGJE E SAKTË! 🎉",
  "ten_correct" to "🎵 përgjigje të sakta! KOHA PËR MUZIKË!","wrong" to "Nuk është e saktë. Provo përsëri!","wrong_final" to "3 GABIME! -2 PIKË. Pyetja tjetër…",
  "first_letter" to "SHKRONJA E PARË: ","next_level" to "NIVELI TJETËR HAPET AUTOMATIKISHT…","dark_light" to "Tema e errët / Tema e çelët",
  "background_music" to "Muzikë në sfond","game_sounds" to "Tingujt e lojës","music_volume" to "Volumi i muzikës: ",
  "effects_volume" to "Volumi i efekteve zanore: ","volume_help" to "Mund të rregullosh veçmas muzikën dhe efektet zanore.",
  "exit_title" to "Të dalësh nga loja?","exit_confirm" to "A je i sigurt që dëshiron ta mbyllësh aplikacionin?","enter_word" to "Shkruaj fjalën e saktë!"
 )
 val mk=mapOf(
  "words_photos" to "зборови со фотографии","level" to "НИВО","update" to "АЖУРИРАЊЕ","checking" to "Проверка за ажурирање…",
  "no_release" to "Нема објавена верзија засега.","latest" to "Ја имаш најновата верзија","found" to "Пронајдена е нова верзија. Се презема…",
  "failed" to "Проверката не успеа. Провери интернет и обиди се повторно.","correct" to "БРАВО! ТОЧЕН ОДГОВОР! 🎉",
  "ten_correct" to "🎵 ТОЧНИ ОДГОВОРИ! ВРЕМЕ Е ЗА МУЗИКА!","wrong" to "Не е точно. Обиди се повторно!","wrong_final" to "3 ПОГРЕШНИ ОБИДИ! -2 ПОЕНИ. Следното прашање…",
  "first_letter" to "ПРВА БУКВА: ","next_level" to "СЛЕДНОТО НИВО СЕ ОТВОРА АВТОМАТСКИ…","dark_light" to "Темна тема / Светла тема",
  "background_music" to "Музика во позадина","game_sounds" to "Звуци во играта","music_volume" to "Јачина на музиката: ",
  "effects_volume" to "Јачина на звучните ефекти: ","volume_help" to "Можеш одделно да ја намалиш или зголемиш музиката и звуците.",
  "exit_title" to "Излез од играта?","exit_confirm" to "Дали сигурно сакаш да ја затвориш апликацијата?","enter_word" to "Внеси го точниот збор!"
 )
 return when(lang) { "fr"->fr[key]; "sq"->sq[key]; "en"->en[key]; else->mk[key] } ?: key
}

private val frenchAnimalWords = mapOf(
 "КУЧЕ" to "CHIEN","МАЧКА" to "CHAT","ЛАВ" to "LION","ЗЕБРА" to "ZÈBRE",
 "МАЈМУН" to "SINGE","МЕЧКА" to "OURS","ВОЛК" to "LOUP","ДЕЛФИН" to "DAUPHIN",
 "ТИГАР" to "TIGRE","ЛИСИЦА" to "RENARD","ЗАЈАК" to "LAPIN","КОЊ" to "CHEVAL",
 "КРАВА" to "VACHE","ОВЦА" to "MOUTON","КОЗА" to "CHÈVRE","ЖЕЛКА" to "TORTUE",
 "ПАПАГАЛ" to "PERROQUET","ОРЕЛ" to "AIGLE","СЛОН" to "ÉLÉPHANT","ЖИРАФА" to "GIRAFE",
 "НОСОРОГ" to "RHINOCÉROS","НИЛСКИ КОЊ" to "HIPPOPOTAME","КЕНГУР" to "KANGOUROU",
 "ПИНГВИН" to "PINGOUIN","БУВ" to "HIBOU","ПАУН" to "PAON","ЕЖ" to "HÉRISSON",
 "ВЕРВЕРИЦА" to "ÉCUREUIL","КАМИЛА" to "CHAMEAU","ПАНДА" to "PANDA","КОАЛА" to "KOALA",
 "КРОКОДИЛ" to "CROCODILE","ЗМИЈА" to "SERPENT","ЖАБА" to "GRENOUILLE",
 "ПЧЕЛА" to "ABEILLE","ПЕПЕРУТКА" to "PAPILLON","МИШКА" to "SOURIS","СВИЊА" to "COCHON"
)
private val frenchAnimalClues = mapOf(
 "КУЧЕ" to "Animal domestique qui aboie.",
 "МАЧКА" to "Animal domestique qui miaule.",
 "ЛАВ" to "Grand félin sauvage qui vit en Afrique.",
 "ЗЕБРА" to "Animal africain reconnaissable à ses rayures noires et blanches.",
 "МАЈМУН" to "Animal qui grimpe aux arbres.",
 "МЕЧКА" to "Grand animal forestier qui marche à quatre pattes.",
 "ВОЛК" to "Animal sauvage ressemblant à un chien et vivant en meute.",
 "ДЕЛФИН" to "Animal marin intelligent qui nage et saute hors de l’eau.",
 "ТИГАР" to "Grand félin sauvage au pelage orange rayé de noir.",
 "ЛИСИЦА" to "Animal sauvage au museau pointu et à la queue touffue.",
 "ЗАЈАК" to "Petit animal aux longues oreilles qui bondit.",
 "КОЊ" to "Grand animal domestique que l’on peut monter.",
 "КРАВА" to "Animal domestique qui produit du lait.",
 "ОВЦА" to "Animal domestique couvert de laine.",
 "КОЗА" to "Animal domestique qui grimpe sur les rochers.",
 "ЖЕЛКА" to "Animal qui possède une carapace dure sur le dos.",
 "ПАПАГАЛ" to "Oiseau coloré capable d’imiter des sons.",
 "ОРЕЛ" to "Grand oiseau de proie aux serres puissantes.",
 "СЛОН" to "Grand animal doté d’une longue trompe et de grandes oreilles.",
 "ЖИРАФА" to "Grand animal africain au long cou.",
 "НОСОРОГ" to "Grand animal à la peau épaisse et portant une corne sur le nez.",
 "НИЛСКИ КОЊ" to "Grand animal africain qui vit dans l’eau et près des rivières.",
 "КЕНГУР" to "Animal australien qui saute et porte son petit dans une poche.",
 "ПИНГВИН" to "Oiseau qui ne vole pas, mais nage très bien dans les mers froides.",
 "БУВ" to "Oiseau nocturne aux grands yeux.",
 "ПАУН" to "Oiseau connu pour sa grande queue colorée.",
 "ЕЖ" to "Petit animal recouvert de piquants.",
 "ВЕРВЕРИЦА" to "Petit animal qui grimpe aux arbres et ramasse des noix.",
 "КАМИЛА" to "Animal doté d’une bosse qui peut rester longtemps sans boire.",
 "ПАНДА" to "Ours noir et blanc qui mange principalement du bambou.",
 "КОАЛА" to "Animal marsupial australien qui vit dans les arbres.",
 "КРОКОДИЛ" to "Grand reptile aux mâchoires puissantes qui vit dans l’eau et sur terre.",
 "ЗМИЈА" to "Reptile sans pattes qui se déplace en rampant.",
 "ЖАБА" to "Petit amphibien qui saute et coasse.",
 "ПЧЕЛА" to "Insecte qui récolte le nectar et produit du miel.",
 "ПЕПЕРУТКА" to "Insecte aux grandes ailes colorées.",
 "МИШКА" to "Petit rongeur doté d’une longue queue.",
 "СВИЊА" to "Animal domestique qui vit souvent à la ferme et fouille le sol avec son groin."
)

private val frenchFoodWords = mapOf(
 "ЈАБОЛКО" to "POMME","БАНАНА" to "BANANE","ПОРТОКАЛ" to "ORANGE","ЈАГОДА" to "FRAISE",
 "ЛИМОН" to "CITRON","ЛУБЕНИЦА" to "PASTÈQUE","АНАНАС" to "ANANAS","МОРКОВ" to "CAROTTE",
 "КРУША" to "POIRE","ЦРЕША" to "CERISE","ПРАСКА" to "PÊCHE","ГРОЗЈЕ" to "RAISIN",
 "КИВИ" to "KIWI","КАЈСИЈА" to "ABRICOT","ДОМАТ" to "TOMATE","КРАСТАВИЦА" to "CONCOMBRE",
 "КОМПИР" to "POMME DE TERRE","ОРИЗ" to "RIZ","МАНДАРИНА" to "MANDARINE","ЛОЗОВ ЛИСТ" to "FEUILLE DE VIGNE",
 "СМОКВА" to "FIGUE","КАЛИНКА" to "GRENADE","ДИЊА" to "MELON","ПИПЕРКА" to "POIVRON",
 "КРОМИД" to "OIGNON","ЛУК" to "AIL","СИРЕЊЕ" to "FROMAGE","ЛЕБ" to "PAIN",
 "МАЛИНА" to "FRAMBOISE","БОРОВИНКА" to "MYRTILLE","КАПИНА" to "MÛRE","ПАПАЈА" to "PAPAYE",
 "МАНГО" to "MANGUE","АВОКАДО" to "AVOCAT","ПЕЧУРКА" to "CHAMPIGNON","ПЧЕНКА" to "MAÏS",
 "ТЕСТЕНИНИ" to "PÂTES","ЈОГУРТ" to "YAOURT"
)
private val frenchFoodClues = mapOf(
 "ЈАБОЛКО" to "Fruit rouge ou vert qui pousse sur un arbre.",
 "БАНАНА" to "Fruit jaune que l’on épluche avant de le manger.",
 "ПОРТОКАЛ" to "Fruit rond et agrume à la peau orange.",
 "ЈАГОДА" to "Fruit rouge avec de petites graines à sa surface.",
 "ЛИМОН" to "Agrume jaune au goût acide.",
 "ЛУБЕНИЦА" to "Gros fruit d’été à la chair rouge et juteuse.",
 "АНАНАС" to "Fruit tropical à la peau dure et aux feuilles vertes.",
 "МОРКОВ" to "Légume-racine orange que l’on mange cru ou cuit.",
 "КРУША" to "Fruit juteux qui a souvent la forme d’une cloche.",
 "ЦРЕША" to "Petit fruit rouge avec un noyau dur.",
 "ПРАСКА" to "Fruit à la peau duveteuse et au gros noyau.",
 "ГРОЗЈЕ" to "Petits fruits qui poussent en grappes.",
 "КИВИ" to "Fruit à la peau brune et à la chair verte.",
 "КАЈСИЈА" to "Fruit orange à la peau douce avec un seul noyau.",
 "ДОМАТ" to "Fruit rouge souvent utilisé dans les salades.",
 "КРАСТАВИЦА" to "Long légume vert souvent mangé cru.",
 "КОМПИР" to "Légume riche en amidon que l’on fait bouillir ou cuire au four.",
 "ОРИЗ" to "Petits grains que l’on cuisine en accompagnement ou en plat.",
 "МАНДАРИНА" to "Petit agrume facile à éplucher.",
 "ЛОЗОВ ЛИСТ" to "Feuille utilisée pour préparer des feuilles de vigne farcies.",
 "СМОКВА" to "Fruit sucré à la chair tendre et contenant de nombreuses petites graines.",
 "КАЛИНКА" to "Fruit qui contient de petites graines rouges.",
 "ДИЊА" to "Gros fruit d’été sucré à la chair parfumée.",
 "ПИПЕРКА" to "Légume qui peut être rouge, jaune ou vert.",
 "КРОМИД" to "Légume que l’on coupe souvent pour cuisiner et qui fait pleurer.",
 "ЛУК" to "Légume aromatique à l’odeur forte, composé de gousses.",
 "СИРЕЊЕ" to "Produit laitier fabriqué à partir du lait.",
 "ЛЕБ" to "Aliment de base cuit au four avec de la farine et de l’eau.",
 "МАЛИНА" to "Petit fruit des bois rouge ou rose.",
 "БОРОВИНКА" to "Petit fruit des bois de couleur bleu foncé.",
 "КАПИНА" to "Fruit sombre qui pousse sur un arbuste épineux.",
 "ПАПАЈА" to "Fruit tropical à la chair orange et aux graines noires.",
 "МАНГО" to "Fruit tropical à la chair juteuse jaune orangé.",
 "АВОКАДО" to "Fruit vert avec un gros noyau et une chair crémeuse.",
 "ПЕЧУРКА" to "Champignon comestible utilisé dans de nombreux plats.",
 "ПЧЕНКА" to "Grains jaunes qui poussent sur un épi.",
 "ТЕСТЕНИНИ" to "Aliment à base de pâte, souvent en forme de spaghetti.",
 "ЈОГУРТ" to "Produit laitier au goût légèrement acidulé."
)


private val frenchOtherWords = mapOf(
 "ПЛАНИНА" to "MONTAGNE","СОНЦЕ" to "SOLEIL","РЕКА" to "RIVIÈRE","ВОДОПАД" to "CASCADE","ЕЗЕРО" to "LAC","ПЕШТЕРА" to "GROTTE","ВУЛКАН" to "VOLCAN","ОСТРОВ" to "ÎLE","ШУМА" to "FORÊT","ЦВЕТ" to "FLEUR","ДРВО" to "ARBRE","ОБЛАК" to "NUAGE","ДОЖД" to "PLUIE","СНЕГ" to "NEIGE","ВЕТЕР" to "VENT","МОРЕ" to "MER","ПЕСОК" to "SABLE","ЅВЕЗДА" to "ÉTOILE","ДОЛИНА" to "VALLÉE","РИД" to "COLLINE","КАНЈОН" to "CANYON","МОЛЊА" to "ÉCLAIR","ВИНОЖИТО" to "ARC-EN-CIEL","МАГЛА" to "BROUILLARD","ИЗВОР" to "SOURCE","КРАЈБРЕЖЈЕ" to "LITTORAL","ПУСТИНА" to "DÉSERT","МОЧУРИШТЕ" to "MARAIS","ВОДЕНА ЛИЛЈАНА" to "NÉNUPHAR","КОРАЛЕН ГРЕБЕН" to "RÉCIF CORALLIEN","ГРОМ" to "TONNERRE","МРАЗ" to "GEL","СЕНКА" to "OMBRE","ПРИЛИВ" to "MARÉE MONTANTE","СЕВЕРНА СВЕТЛИНА" to "AURORE BORÉALE",
 "АВТОМОБИЛ" to "VOITURE","ВЕЛОСИПЕД" to "VÉLO","ВОЗ" to "TRAIN","МОТОЦИКЛ" to "MOTO","ТРАМВАЈ" to "TRAMWAY","ХЕЛИКОПТЕР" to "HÉLICOPTÈRE","ТРОТИНЕТ" to "TROTTINETTE","КАМИОН" to "CAMION","АВИОН" to "AVION","БРОД" to "BATEAU","АВТОБУС" to "AUTOBUS","ТАКСИ" to "TAXI","МЕТРО" to "MÉTRO","ЧАМЕЦ" to "BARQUE","ЕДРИЛИЦА" to "VOILIER","СКУТЕР" to "SCOOTER","ПРИКОЛКА" to "REMORQUE","БАГЕР" to "PELLETEUSE","ТРОЛЕЈБУС" to "TROLLEYBUS","ТРКАЛО" to "ROUE","ПОДМОРНИЦА" to "SOUS-MARIN","ЖИЧНИЦА" to "TÉLÉPHÉRIQUE","САНКА" to "TRAÎNEAU","КАРАВАН" to "CARAVANE","КОЧИЈА" to "CALÈCHE","БУЛДОЖЕР" to "BULLDOZER","КОМБЕ" to "FOURGON","ТРАКТОР" to "TRACTEUR","САМОЛЕТ" to "AVION","ГЛИСЕР" to "BATEAU À MOTEUR","ФЕРИБРОД" to "FERRY","ДИРИЖАБЛ" to "DIRIGEABLE","ПАРЕН ВОЗ" to "LOCOMOTIVE À VAPEUR","РОЛЕРИ" to "ROLLERS","СКЕЈТБОРД" to "SKATEBOARD","СНЕГОМОБИЛ" to "MOTONEIGE",
 "ФУДБАЛ" to "FOOTBALL","КОШАРКА" to "BASKET-BALL","ПЛИВАЊЕ" to "NATATION","ОДБОЈКА" to "VOLLEY-BALL","СКИЈАЊЕ" to "SKI","ВЕЛОСИПЕДИЗАМ" to "CYCLISME","АТЛЕТИКА" to "ATHLÉTISME","ГИМНАСТИКА" to "GYMNASTIQUE","ТЕНИС" to "TENNIS","БОКС" to "BOXE","РАКОМЕТ" to "HANDBALL","БЕЈЗБОЛ" to "BASEBALL","ГОЛФ" to "GOLF","КАРАТЕ" to "KARATÉ","ЏУДО" to "JUDO","ЈАВАЊЕ" to "ÉQUITATION","КАЈАК" to "KAYAK","КЛИЗГАЊЕ" to "PATINAGE","ПИНГ-ПОНГ" to "PING-PONG","КУГЛАЊЕ" to "BOWLING","СТРЕЛАШТВО" to "TIR À L’ARC","ФЕХТУВАЊЕ" to "ESCRIME","РЕГБИ" to "RUGBY","КРИКЕТ" to "CRICKET","СНОУБОРД" to "SNOWBOARD","СУРФАЊЕ" to "SURF","МАРАТОН" to "MARATHON","КАРЛИНГ" to "CURLING","ФУТСАЛ" to "FUTSAL","ВЕСЛАЊЕ" to "AVIRON","ТРИАТЛОН" to "TRIATHLON","ПАРКУР" to "PARKOUR","КАЧУВАЊЕ" to "ESCALADE","ДИГАЊЕ ТЕГОВИ" to "HALTÉROPHILIE","БОРЕЊЕ" to "LUTTE","БАДМИНТОН" to "BADMINTON","СЕПАКТАКРАВ" to "SEPAK-TAKRAW","СОФТБОЛ" to "SOFTBALL",
 "УЧИЛИШТЕ" to "ÉCOLE","БИБЛИОТЕКА" to "BIBLIOTHÈQUE","БОЛНИЦА" to "HÔPITAL","ТЕАТАР" to "THÉÂTRE","СТАДИОН" to "STADE","АЕРОДРОМ" to "AÉROPORT","ЖЕЛЕЗНИЧКА СТАНИЦА" to "GARE","ПАЗАР" to "MARCHÉ","МУЗЕЈ" to "MUSÉE","ПЛАЖА" to "PLAGE","ЦРКВА" to "ÉGLISE","ЏАМИЈА" to "MOSQUÉE","РЕСТОРАН" to "RESTAURANT","ХОТЕЛ" to "HÔTEL","БАНКА" to "BANQUE","ПОШТА" to "BUREAU DE POSTE","ПАРК" to "PARC","ФАРМА" to "FERME","БЕНЗИНСКА ПУМПА" to "STATION-SERVICE","АПТЕКА" to "PHARMACIE","ПЕКАРНИЦА" to "BOULANGERIE","СУПЕРМАРКЕТ" to "SUPERMARCHÉ","ПОЖАРНА СТАНИЦА" to "CASERNE DE POMPIERS","ПОЛИЦИСКА СТАНИЦА" to "COMMISSARIAT","ГРАДИНКА" to "CRÈCHE","АВТОБУСКА СТАНИЦА" to "GARE ROUTIÈRE","ЗООЛОШКА ГРАДИНА" to "ZOO","БАЗЕН" to "PISCINE","ОПШТИНА" to "MAIRIE","СУД" to "TRIBUNAL","ЗАТВОР" to "PRISON","УНИВЕРЗИТЕТ" to "UNIVERSITÉ","ЛАБОРАТОРИЈА" to "LABORATOIRE","ОПЕРСКА КУЌА" to "OPÉRA","КИНО" to "CINÉMA","ЗАБАВЕН ПАРК" to "PARC D’ATTRACTIONS","ПРИСТАНИШТЕ" to "PORT","СВЕТИЛНИК" to "PHARE"
)
private val englishWords = mapOf(
 "КУЧЕ" to "DOG","МАЧКА" to "CAT","ЛАВ" to "LION","ЗЕБРА" to "ZEBRA","МАЈМУН" to "MONKEY","МЕЧКА" to "BEAR","ВОЛК" to "WOLF","ДЕЛФИН" to "DOLPHIN","ТИГАР" to "TIGER","ЛИСИЦА" to "FOX","ЗАЈАК" to "RABBIT","КОЊ" to "HORSE","КРАВА" to "COW","ОВЦА" to "SHEEP","КОЗА" to "GOAT","ЖЕЛКА" to "TURTLE","ПАПАГАЛ" to "PARROT","ОРЕЛ" to "EAGLE","СЛОН" to "ELEPHANT","ЖИРАФА" to "GIRAFFE","НОСОРОГ" to "RHINOCEROS","НИЛСКИ КОЊ" to "HIPPOPOTAMUS","КЕНГУР" to "KANGAROO","ПИНГВИН" to "PENGUIN","БУВ" to "OWL","ПАУН" to "PEACOCK","ЕЖ" to "HEDGEHOG","ВЕРВЕРИЦА" to "SQUIRREL","КАМИЛА" to "CAMEL","ПАНДА" to "PANDA","КОАЛА" to "KOALA","КРОКОДИЛ" to "CROCODILE","ЗМИЈА" to "SNAKE","ЖАБА" to "FROG","ПЧЕЛА" to "BEE","ПЕПЕРУТКА" to "BUTTERFLY","МИШКА" to "MOUSE","СВИЊА" to "PIG",
 "ЈАБОЛКО" to "APPLE","БАНАНА" to "BANANA","ПОРТОКАЛ" to "ORANGE","ЈАГОДА" to "STRAWBERRY","ЛИМОН" to "LEMON","ЛУБЕНИЦА" to "WATERMELON","АНАНАС" to "PINEAPPLE","МОРКОВ" to "CARROT","КРУША" to "PEAR","ЦРЕША" to "CHERRY","ПРАСКА" to "PEACH","ГРОЗЈЕ" to "GRAPES","КИВИ" to "KIWI","КАЈСИЈА" to "APRICOT","ДОМАТ" to "TOMATO","КРАСТАВИЦА" to "CUCUMBER","КОМПИР" to "POTATO","ОРИЗ" to "RICE","МАНДАРИНА" to "MANDARIN","ЛОЗОВ ЛИСТ" to "GRAPE LEAF","СМОКВА" to "FIG","КАЛИНКА" to "POMEGRANATE","ДИЊА" to "MELON","ПИПЕРКА" to "BELL PEPPER","КРОМИД" to "ONION","ЛУК" to "GARLIC","СИРЕЊЕ" to "CHEESE","ЛЕБ" to "BREAD","МАЛИНА" to "RASPBERRY","БОРОВИНКА" to "BLUEBERRY","КАПИНА" to "BLACKBERRY","ПАПАЈА" to "PAPAYA","МАНГО" to "MANGO","АВОКАДО" to "AVOCADO","ПЕЧУРКА" to "MUSHROOM","ПЧЕНКА" to "CORN","ТЕСТЕНИНИ" to "PASTA","ЈОГУРТ" to "YOGURT",
 "ПЛАНИНА" to "MOUNTAIN","СОНЦЕ" to "SUN","РЕКА" to "RIVER","ВОДОПАД" to "WATERFALL","ЕЗЕРО" to "LAKE","ПЕШТЕРА" to "CAVE","ВУЛКАН" to "VOLCANO","ОСТРОВ" to "ISLAND","ШУМА" to "FOREST","ЦВЕТ" to "FLOWER","ДРВО" to "TREE","ОБЛАК" to "CLOUD","ДОЖД" to "RAIN","СНЕГ" to "SNOW","ВЕТЕР" to "WIND","МОРЕ" to "SEA","ПЕСОК" to "SAND","ЅВЕЗДА" to "STAR","ДОЛИНА" to "VALLEY","РИД" to "HILL","КАНЈОН" to "CANYON","МОЛЊА" to "LIGHTNING","ВИНОЖИТО" to "RAINBOW","МАГЛА" to "FOG","ИЗВОР" to "SPRING","КРАЈБРЕЖЈЕ" to "COAST","ПУСТИНА" to "DESERT","МОЧУРИШТЕ" to "SWAMP","ВОДЕНА ЛИЛЈАНА" to "WATER LILY","КОРАЛЕН ГРЕБЕН" to "CORAL REEF","ГРОМ" to "THUNDER","МРАЗ" to "FROST","СЕНКА" to "SHADOW","ПРИЛИВ" to "HIGH TIDE","СЕВЕРНА СВЕТЛИНА" to "NORTHERN LIGHTS",
 "АВТОМОБИЛ" to "CAR","ВЕЛОСИПЕД" to "BICYCLE","ВОЗ" to "TRAIN","МОТОЦИКЛ" to "MOTORCYCLE","ТРАМВАЈ" to "TRAM","ХЕЛИКОПТЕР" to "HELICOPTER","ТРОТИНЕТ" to "SCOOTER","КАМИОН" to "TRUCK","АВИОН" to "AIRPLANE","БРОД" to "SHIP","АВТОБУС" to "BUS","ТАКСИ" to "TAXI","МЕТРО" to "SUBWAY","ЧАМЕЦ" to "BOAT","ЕДРИЛИЦА" to "SAILBOAT","СКУТЕР" to "MOPED","ПРИКОЛКА" to "TRAILER","БАГЕР" to "EXCAVATOR","ТРОЛЕЈБУС" to "TROLLEYBUS","ТРКАЛО" to "WHEEL","ПОДМОРНИЦА" to "SUBMARINE","ЖИЧНИЦА" to "CABLE CAR","САНКА" to "SLED","КАРАВАН" to "CARAVAN","КОЧИЈА" to "CARRIAGE","БУЛДОЖЕР" to "BULLDOZER","КОМБЕ" to "VAN","ТРАКТОР" to "TRACTOR","САМОЛЕТ" to "PLANE","ГЛИСЕР" to "SPEEDBOAT","ФЕРИБРОД" to "FERRY","ДИРИЖАБЛ" to "AIRSHIP","ПАРЕН ВОЗ" to "STEAM TRAIN","РОЛЕРИ" to "ROLLER SKATES","СКЕЈТБОРД" to "SKATEBOARD","СНЕГОМОБИЛ" to "SNOWMOBILE",
 "ФУДБАЛ" to "FOOTBALL","КОШАРКА" to "BASKETBALL","ПЛИВАЊЕ" to "SWIMMING","ОДБОЈКА" to "VOLLEYBALL","СКИЈАЊЕ" to "SKIING","ВЕЛОСИПЕДИЗАМ" to "CYCLING","АТЛЕТИКА" to "ATHLETICS","ГИМНАСТИКА" to "GYMNASTICS","ТЕНИС" to "TENNIS","БОКС" to "BOXING","РАКОМЕТ" to "HANDBALL","БЕЈЗБОЛ" to "BASEBALL","ГОЛФ" to "GOLF","КАРАТЕ" to "KARATE","ЏУДО" to "JUDO","ЈАВАЊЕ" to "HORSE RIDING","КАЈАК" to "KAYAKING","КЛИЗГАЊЕ" to "SKATING","ПИНГ-ПОНГ" to "TABLE TENNIS","КУГЛАЊЕ" to "BOWLING","СТРЕЛАШТВО" to "ARCHERY","ФЕХТУВАЊЕ" to "FENCING","РЕГБИ" to "RUGBY","КРИКЕТ" to "CRICKET","СНОУБОРД" to "SNOWBOARDING","СУРФАЊЕ" to "SURFING","МАРАТОН" to "MARATHON","КАРЛИНГ" to "CURLING","ФУТСАЛ" to "FUTSAL","ВЕСЛАЊЕ" to "ROWING","ТРИАТЛОН" to "TRIATHLON","ПАРКУР" to "PARKOUR","КАЧУВАЊЕ" to "CLIMBING","ДИГАЊЕ ТЕГОВИ" to "WEIGHTLIFTING","БОРЕЊЕ" to "WRESTLING","БАДМИНТОН" to "BADMINTON","СЕПАКТАКРАВ" to "SEPAK TAKRAW","СОФТБОЛ" to "SOFTBALL",
 "УЧИЛИШТЕ" to "SCHOOL","БИБЛИОТЕКА" to "LIBRARY","БОЛНИЦА" to "HOSPITAL","ТЕАТАР" to "THEATER","СТАДИОН" to "STADIUM","АЕРОДРОМ" to "AIRPORT","ЖЕЛЕЗНИЧКА СТАНИЦА" to "RAILWAY STATION","ПАЗАР" to "MARKET","МУЗЕЈ" to "MUSEUM","ПЛАЖА" to "BEACH","ЦРКВА" to "CHURCH","ЏАМИЈА" to "MOSQUE","РЕСТОРАН" to "RESTAURANT","ХОТЕЛ" to "HOTEL","БАНКА" to "BANK","ПОШТА" to "POST OFFICE","ПАРК" to "PARK","ФАРМА" to "FARM","БЕНЗИНСКА ПУМПА" to "GAS STATION","АПТЕКА" to "PHARMACY","ПЕКАРНИЦА" to "BAKERY","СУПЕРМАРКЕТ" to "SUPERMARKET","ПОЖАРНА СТАНИЦА" to "FIRE STATION","ПОЛИЦИСКА СТАНИЦА" to "POLICE STATION","ГРАДИНКА" to "KINDERGARTEN","АВТОБУСКА СТАНИЦА" to "BUS STATION","ЗООЛОШКА ГРАДИНА" to "ZOO","БАЗЕН" to "SWIMMING POOL","ОПШТИНА" to "MUNICIPALITY","СУД" to "COURT","ЗАТВОР" to "PRISON","УНИВЕРЗИТЕТ" to "UNIVERSITY","ЛАБОРАТОРИЈА" to "LABORATORY","ОПЕРСКА КУЌА" to "OPERA HOUSE","КИНО" to "CINEMA","ЗАБАВЕН ПАРК" to "AMUSEMENT PARK","ПРИСТАНИШТЕ" to "HARBOR","СВЕТИЛНИК" to "LIGHTHOUSE"
)
private val albanianWords = mapOf(
 "КУЧЕ" to "QEN","МАЧКА" to "MACË","ЛАВ" to "LUAN","ЗЕБРА" to "ZEBRË","МАЈМУН" to "MAJMUN","МЕЧКА" to "ARI","ВОЛК" to "UJK","ДЕЛФИН" to "DELFIN","ТИГАР" to "TIGËR","ЛИСИЦА" to "DHELPËR","ЗАЈАК" to "LEPUR","КОЊ" to "KUAJ","КРАВА" to "LOPË","ОВЦА" to "DELE","КОЗА" to "DHI","ЖЕЛКА" to "BRESHKË","ПАПАГАЛ" to "PAPAGALL","ОРЕЛ" to "SHQIPONJË","СЛОН" to "ELEFANT","ЖИРАФА" to "GJIRAFË","НОСОРОГ" to "RINOCERONT","НИЛСКИ КОЊ" to "HIPOPOTAM","КЕНГУР" to "KANGUR","ПИНГВИН" to "PENGUIN","БУВ" to "BUF","ПАУН" to "PALLUA","ЕЖ" to "IRIQ","ВЕРВЕРИЦА" to "KETËR","КАМИЛА" to "DEVE","ПАНДА" to "PANDA","КОАЛА" to "KOALA","КРОКОДИЛ" to "KROKODIL","ЗМИЈА" to "GJARPËR","ЖАБА" to "BRETËKOSË","ПЧЕЛА" to "BLETË","ПЕПЕРУТКА" to "FLUTUR","МИШКА" to "MI","СВИЊА" to "DERR",
 "ЈАБОЛКО" to "MOLLË","БАНАНА" to "BANANE","ПОРТОКАЛ" to "PORTOKALL","ЈАГОДА" to "LULESHTRYDHE","ЛИМОН" to "LIMON","ЛУБЕНИЦА" to "SHALQI","АНАНАС" to "ANANAS","МОРКОВ" to "KAROTË","КРУША" to "DARDHË","ЦРЕША" to "QERSHI","ПРАСКА" to "PJESHKË","ГРОЗЈЕ" to "RRUSH","КИВИ" to "KIWI","КАЈСИЈА" to "KAJSI","ДОМАТ" to "DOMATE","КРАСТАВИЦА" to "KASTRAVEC","КОМПИР" to "PATATE","ОРИЗ" to "ORIZ","МАНДАРИНА" to "MANDARINË","ЛОЗОВ ЛИСТ" to "GJETHE RRUSHI","СМОКВА" to "FIQ","КАЛИНКА" to "SHEGË","ДИЊА" to "PJEPËR","ПИПЕРКА" to "SPEC","КРОМИД" to "QEPË","ЛУК" to "HUDHËR","СИРЕЊЕ" to "DJATHË","ЛЕБ" to "BUKË","МАЛИНА" to "MJEDËR","БОРОВИНКА" to "BORONICË","КАПИНА" to "MANAFERRË","ПАПАЈА" to "PAPAJË","МАНГО" to "MANGO","АВОКАДО" to "AVOKADO","ПЕЧУРКА" to "KËRPUDHË","ПЧЕНКА" to "MISËR","ТЕСТЕНИНИ" to "MAKARONA","ЈОГУРТ" to "JOGURT",
 "ПЛАНИНА" to "MAL","СОНЦЕ" to "DIELL","РЕКА" to "LUMË","ВОДОПАД" to "UJËVARË","ЕЗЕРО" to "LIQEN","ПЕШТЕРА" to "SHPELLË","ВУЛКАН" to "VULLKAN","ОСТРОВ" to "ISHULL","ШУМА" to "PYLL","ЦВЕТ" to "LULE","ДРВО" to "PEMË","ОБЛАК" to "RE","ДОЖД" to "SHI","СНЕГ" to "BORË","ВЕТЕР" to "ERË","МОРЕ" to "DET","ПЕСОК" to "RËRË","ЅВЕЗДА" to "YLL","ДОЛИНА" to "LUGINË","РИД" to "KODËR","КАНЈОН" to "KANION","МОЛЊА" to "RRUFE","ВИНОЖИТО" to "YLBER","МАГЛА" to "MJEGULL","ИЗВОР" to "BURIM","КРАЈБРЕЖЈЕ" to "BREGDET","ПУСТИНА" to "SHKRETËTIRË","МОЧУРИШТЕ" to "KËNETË","ВОДЕНА ЛИЛЈАНА" to "NILUFER","КОРАЛЕН ГРЕБЕН" to "SHKËMB KORALOR","ГРОМ" to "BUBULLIMË","МРАЗ" to "BRICË","СЕНКА" to "HIJE","ПРИЛИВ" to "BATICA","СЕВЕРНА СВЕТЛИНА" to "DRITAT VERIORE",
 "АВТОМОБИЛ" to "MAKINË","ВЕЛОСИПЕД" to "BICIKLETË","ВОЗ" to "TREN","МОТОЦИКЛ" to "MOTOR","ТРАМВАЈ" to "TRAMVAJ","ХЕЛИКОПТЕР" to "HELIKOPTER","ТРОТИНЕТ" to "SKUTER","КАМИОН" to "KAMION","АВИОН" to "AVION","БРОД" to "ANIJE","АВТОБУС" to "AUTOBUS","ТАКСИ" to "TAKSI","МЕТРО" to "METRO","ЧАМЕЦ" to "VARKË","ЕДРИЛИЦА" to "VELIERË","СКУТЕР" to "SKUTER","ПРИКОЛКА" to "RIMORKIO","БАГЕР" to "EKSKAVATOR","ТРОЛЕЈБУС" to "TROLEJBUS","ТРКАЛО" to "RROTË","ПОДМОРНИЦА" to "NËNDETËSE","ЖИЧНИЦА" to "TELEFERIK","САНКА" to "SLITË","КАРАВАН" to "KARAVAN","КОЧИЈА" to "KARROCË","БУЛДОЖЕР" to "BULDOZER","КОМБЕ" to "FURGON","ТРАКТОР" to "TRAKTOR","САМОЛЕТ" to "AEROPLAN","ГЛИСЕР" to "SKAF ME MOTOR","ФЕРИБРОД" to "FERRY","ДИРИЖАБЛ" to "AEROSTAT","ПАРЕН ВОЗ" to "TREN ME AVULL","РОЛЕРИ" to "PATINA ME RROTA","СКЕЈТБОРД" to "SKATEBOARD","СНЕГОМОБИЛ" to "MOTORRË BORË",
 "ФУДБАЛ" to "FUTBOLL","КОШАРКА" to "BASKETBOLL","ПЛИВАЊЕ" to "NOT","ОДБОЈКА" to "VOLEJBOLL","СКИЈАЊЕ" to "SKI","ВЕЛОСИПЕДИЗАМ" to "ÇIKLIZËM","АТЛЕТИКА" to "ATLETIKË","ГИМНАСТИКА" to "GJIMNASTIKË","ТЕНИС" to "TENIS","БОКС" to "BOKS","РАКОМЕТ" to "HENDBOLL","БЕЈЗБОЛ" to "BEJZBOLL","ГОЛФ" to "GOLF","КАРАТЕ" to "KARATE","ЏУДО" to "XHUDO","ЈАВАЊЕ" to "KALËRIM","КАЈАК" to "KAJAK","КЛИЗГАЊЕ" to "PATINAZH","ПИНГ-ПОНГ" to "PING-PONG","КУГЛАЊЕ" to "BOWLING","СТРЕЛАШТВО" to "GJUANJE ME HARK","ФЕХТУВАЊЕ" to "SKERMË","РЕГБИ" to "RUGBI","КРИКЕТ" to "KRIKET","СНОУБОРД" to "SNOWBOARD","СУРФАЊЕ" to "SURF","МАРАТОН" to "MARATON","КАРЛИНГ" to "CURLING","ФУТСАЛ" to "FUTSALL","ВЕСЛАЊЕ" to "VOZITJE ME VARKË","ТРИАТЛОН" to "TRIATLON","ПАРКУР" to "PARKOUR","КАЧУВАЊЕ" to "NGJITJE","ДИГАЊЕ ТЕГОВИ" to "NGRITJE PESHASH","БОРЕЊЕ" to "MUNDJE","БАДМИНТОН" to "BADMINTON","СЕПАКТАКРАВ" to "SEPAK TAKRAW","СОФТБОЛ" to "SOFTBOLL",
 "УЧИЛИШТЕ" to "SHKOLLË","БИБЛИОТЕКА" to "BIBLIOTEKË","БОЛНИЦА" to "SPITAL","ТЕАТАР" to "TEATËR","СТАДИОН" to "STADIUM","АЕРОДРОМ" to "AEROPORT","ЖЕЛЕЗНИЧКА СТАНИЦА" to "STACION HEKURUDHOR","ПАЗАР" to "TREG","МУЗЕЈ" to "MUZE","ПЛАЖА" to "PLAZH","ЦРКВА" to "KISHË","ЏАМИЈА" to "XHAMI","РЕСТОРАН" to "RESTORANT","ХОТЕЛ" to "HOTEL","БАНКА" to "BANKË","ПОШТА" to "POSTË","ПАРК" to "PARK","ФАРМА" to "FERMË","БЕНЗИНСКА ПУМПА" to "POMPË BENZINE","АПТЕКА" to "FARMACI","ПЕКАРНИЦА" to "FURRË BUKË","СУПЕРМАРКЕТ" to "SUPERMARKET","ПОЖАРНА СТАНИЦА" to "STACION ZJARRFIKËS","ПОЛИЦИСКА СТАНИЦА" to "STACION POLICIE","ГРАДИНКА" to "KOPSHT FËMIJËSH","АВТОБУСКА СТАНИЦА" to "STACION AUTOBUSI","ЗООЛОШКА ГРАДИНА" to "KOPSHT ZOOLOGJIK","БАЗЕН" to "PISHINË","ОПШТИНА" to "KOMUNË","СУД" to "GJYKATË","ЗАТВОР" to "BURG","УНИВЕРЗИТЕТ" to "UNIVERSITET","ЛАБОРАТОРИЈА" to "LABORATOR","ОПЕРСКА КУЌА" to "SHTËPI OPERE","КИНО" to "KINEMA","ЗАБАВЕН ПАРК" to "PARK ARGËTIMI","ПРИСТАНИШТЕ" to "PORT","СВЕТИЛНИК" to "FANAR"
)


private val englishClues = mapOf(
 "КУЧЕ" to "A domestic animal that barks.",
 "МАЧКА" to "A domestic animal that meows.",
 "ЛАВ" to "A large wild cat that lives in Africa.",
 "ЗЕБРА" to "An African animal with black-and-white stripes.",
 "МАЈМУН" to "An animal that climbs trees.",
 "МЕЧКА" to "A large forest animal that walks on four legs.",
 "ВОЛК" to "A wild dog-like animal that lives in a pack.",
 "ДЕЛФИН" to "An intelligent sea animal that swims and jumps.",
 "ТИГАР" to "A large wild cat with orange fur and black stripes.",
 "ЛИСИЦА" to "A wild animal with a bushy tail and pointed muzzle.",
 "ЗАЈАК" to "A small animal with long ears that hops.",
 "КОЊ" to "A large domestic animal that people can ride.",
 "КРАВА" to "A domestic animal that produces milk.",
 "ОВЦА" to "A farm animal covered in wool.",
 "КОЗА" to "A domestic animal that climbs rocks.",
 "ЖЕЛКА" to "An animal with a hard shell on its back.",
 "ПАПАГАЛ" to "A colorful bird that can imitate sounds.",
 "ОРЕЛ" to "A large bird of prey with strong talons.",
 "СЛОН" to "A large animal with a long trunk and big ears.",
 "ЖИРАФА" to "A tall African animal with a long neck.",
 "НОСОРОГ" to "A large animal with thick skin and a horn on its nose.",
 "НИЛСКИ КОЊ" to "A large African animal that lives in and near water.",
 "КЕНГУР" to "An Australian animal that hops and carries its young in a pouch.",
 "ПИНГВИН" to "A bird that cannot fly but swims very well in cold seas.",
 "БУВ" to "A nocturnal bird with large eyes.",
 "ПАУН" to "A bird known for its large, colorful tail.",
 "ЕЖ" to "A small animal covered in spines.",
 "ВЕРВЕРИЦА" to "A small animal that climbs trees and collects nuts.",
 "КАМИЛА" to "An animal with a hump that can go a long time without water.",
 "ПАНДА" to "A black-and-white bear that mainly eats bamboo.",
 "КОАЛА" to "An Australian marsupial that lives in trees.",
 "КРОКОДИЛ" to "A large reptile with powerful jaws that lives in water and on land.",
 "ЗМИЈА" to "A legless reptile that moves by slithering.",
 "ЖАБА" to "A small amphibian that hops and croaks.",
 "ПЧЕЛА" to "An insect that collects nectar and makes honey.",
 "ПЕПЕРУТКА" to "An insect with large, colorful wings.",
 "МИШКА" to "A small rodent with a long tail.",
 "СВИЊА" to "A farm animal that often roots around with its snout."
)
private val albanianClues = mapOf(
 "КУЧЕ" to "Kafshë shtëpiake që leh.",
 "МАЧКА" to "Kafshë shtëpiake që mjaullin.",
 "ЛАВ" to "Mace e madhe e egër që jeton në Afrikë.",
 "ЗЕБРА" to "Kafshë afrikane me vija bardh e zi.",
 "МАЈМУН" to "Kafshë që ngjitet nëpër pemë.",
 "МЕЧКА" to "Kafshë e madhe pylli që ecën me katër këmbë.",
 "ВОЛК" to "Kafshë e egër e ngjashme me qenin që jeton në tufë.",
 "ДЕЛФИН" to "Kafshë detare inteligjente që noton dhe kërcen.",
 "ТИГАР" to "Mace e madhe e egër me gëzof portokalli dhe vija të zeza.",
 "ЛИСИЦА" to "Kafshë e egër me bisht të dendur dhe feçkë të mprehtë.",
 "ЗАЈАК" to "Kafshë e vogël me veshë të gjatë që kërcen.",
 "КОЊ" to "Kafshë e madhe shtëpiake që mund të kalërohet.",
 "КРАВА" to "Kafshë shtëpiake që prodhon qumësht.",
 "ОВЦА" to "Kafshë ferme e mbuluar me lesh.",
 "КОЗА" to "Kafshë shtëpiake që ngjitet në shkëmbinj.",
 "ЖЕЛКА" to "Kafshë me një guaskë të fortë në shpinë.",
 "ПАПАГАЛ" to "Zog shumëngjyrësh që mund të imitojë tinguj.",
 "ОРЕЛ" to "Shpend i madh grabitqar me kthetra të fuqishme.",
 "СЛОН" to "Kafshë e madhe me feçkë të gjatë dhe veshë të mëdhenj.",
 "ЖИРАФА" to "Kafshë e gjatë afrikane me qafë të gjatë.",
 "НОСОРОГ" to "Kafshë e madhe me lëkurë të trashë dhe bri në hundë.",
 "НИЛСКИ КОЊ" to "Kafshë e madhe afrikane që jeton në ujë dhe pranë tij.",
 "КЕНГУР" to "Kafshë australiane që kërcen dhe mban të voglin në qeskë.",
 "ПИНГВИН" to "Zog që nuk fluturon, por noton shumë mirë në detet e ftohta.",
 "БУВ" to "Shpend nate me sy të mëdhenj.",
 "ПАУН" to "Shpend i njohur për bishtin e madh shumëngjyrësh.",
 "ЕЖ" to "Kafshë e vogël e mbuluar me gjemba.",
 "ВЕРВЕРИЦА" to "Kafshë e vogël që ngjitet në pemë dhe mbledh arra.",
 "КАМИЛА" to "Kafshë me gungë që mund të qëndrojë gjatë pa ujë.",
 "ПАНДА" to "Ari bardhezi që ha kryesisht bambu.",
 "КОАЛА" to "Kafshë marsupiale australiane që jeton në pemë.",
 "КРОКОДИЛ" to "Zvarranik i madh me nofulla të fuqishme që jeton në ujë dhe në tokë.",
 "ЗМИЈА" to "Zvarranik pa këmbë që lëviz duke u zvarritur.",
 "ЖАБА" to "Amfib i vogël që kërcen dhe kakaris.",
 "ПЧЕЛА" to "Insekt që mbledh nektar dhe prodhon mjaltë.",
 "ПЕПЕРУТКА" to "Insekt me krahë të mëdhenj shumëngjyrësh.",
 "МИШКА" to "Brejtës i vogël me bisht të gjatë.",
 "СВИЊА" to "Kafshë ferme që shpesh gërmon tokën me turirin."
)


private val englishFoodClues = mapOf(
 "ЈАБОЛКО" to "A red or green fruit that grows on a tree.",
 "БАНАНА" to "A yellow fruit that you peel before eating.",
 "ПОРТОКАЛ" to "A round citrus fruit with orange skin.",
 "ЈАГОДА" to "A red fruit with tiny seeds on its surface.",
 "ЛИМОН" to "A sour yellow citrus fruit.",
 "ЛУБЕНИЦА" to "A large summer fruit with juicy red flesh.",
 "АНАНАС" to "A tropical fruit with tough skin and green leaves.",
 "МОРКОВ" to "An orange root vegetable eaten raw or cooked.",
 "КРУША" to "A juicy fruit that often has a bell shape.",
 "ЦРЕША" to "A small red fruit with a hard stone.",
 "ПРАСКА" to "A soft fruit with fuzzy skin and a large stone.",
 "ГРОЗЈЕ" to "Small fruits that grow in bunches.",
 "КИВИ" to "A fruit with brown skin and green flesh.",
 "КАЈСИЈА" to "An orange fruit with soft skin and one stone.",
 "ДОМАТ" to "A red fruit often used in salads.",
 "КРАСТАВИЦА" to "A long green vegetable often eaten fresh.",
 "КОМПИР" to "A starchy vegetable that is boiled or baked.",
 "ОРИЗ" to "Small grains cooked as a side dish or a meal.",
 "МАНДАРИНА" to "A small citrus fruit that is easy to peel.",
 "ЛОЗОВ ЛИСТ" to "A leaf used to wrap stuffed vine leaves.",
 "СМОКВА" to "A sweet fruit with soft flesh and many tiny seeds.",
 "КАЛИНКА" to "A fruit filled with small red seeds.",
 "ДИЊА" to "A large, sweet summer fruit with fragrant flesh.",
 "ПИПЕРКА" to "A vegetable that can be red, yellow, or green.",
 "КРОМИД" to "A vegetable often chopped for cooking that makes your eyes water.",
 "ЛУК" to "A strongly scented vegetable made up of cloves.",
 "СИРЕЊЕ" to "A dairy product made from milk.",
 "ЛЕБ" to "A staple food baked from flour and water.",
 "МАЛИНА" to "A small red or pink berry.",
 "БОРОВИНКА" to "A small, dark-blue wild berry.",
 "КАПИНА" to "A dark berry that grows on a thorny bush.",
 "ПАПАЈА" to "A tropical fruit with orange flesh and black seeds.",
 "МАНГО" to "A tropical fruit with juicy yellow-orange flesh.",
 "АВОКАДО" to "A green fruit with a large stone and creamy flesh.",
 "ПЕЧУРКА" to "An edible fungus used in many dishes.",
 "ПЧЕНКА" to "Yellow kernels that grow on a cob.",
 "ТЕСТЕНИНИ" to "Food made from dough, often shaped like spaghetti.",
 "ЈОГУРТ" to "A dairy product with a slightly sour taste."
)
private val albanianFoodClues = mapOf(
 "ЈАБОЛКО" to "Frut i kuq ose i gjelbër që rritet në pemë.",
 "БАНАНА" to "Frut i verdhë që qërohet para se të hahet.",
 "ПОРТОКАЛ" to "Frut agrum i rrumbullakët me lëkurë portokalli.",
 "ЈАГОДА" to "Frut i kuq me fara të vogla në sipërfaqe.",
 "ЛИМОН" to "Frut agrum i verdhë me shije të thartë.",
 "ЛУБЕНИЦА" to "Frut i madh vere me tul të kuq dhe të lëngshëm.",
 "АНАНАС" to "Frut tropikal me lëkurë të fortë dhe gjethe të gjelbra.",
 "МОРКОВ" to "Perime rrënjore portokalli që hahet e freskët ose e zier.",
 "КРУША" to "Frut i lëngshëm që shpesh ka formë zileje.",
 "ЦРЕША" to "Frut i vogël i kuq me bërthamë të fortë.",
 "ПРАСКА" to "Frut i butë me lëkurë kadifeje dhe bërthamë të madhe.",
 "ГРОЗЈЕ" to "Fruta të vogla që rriten në vile.",
 "КИВИ" to "Frut me lëkurë kafe dhe tul të gjelbër.",
 "КАЈСИЈА" to "Frut portokalli me lëkurë të butë dhe një bërthamë.",
 "ДОМАТ" to "Frut i kuq që përdoret shpesh në sallata.",
 "КРАСТАВИЦА" to "Perime e gjatë dhe e gjelbër që hahet shpesh e freskët.",
 "КОМПИР" to "Perime me niseshte që zihet ose piqet.",
 "ОРИЗ" to "Kokrra të vogla që gatuhen si garniturë ose si gjellë.",
 "МАНДАРИНА" to "Frut i vogël agrum që qërohet lehtë.",
 "ЛОЗОВ ЛИСТ" to "Gjethe që përdoret për të mbështjellë japrakë.",
 "СМОКВА" to "Frut i ëmbël me tul të butë dhe shumë fara të imëta.",
 "КАЛИНКА" to "Frut që ka kokrra të vogla të kuqe brenda.",
 "ДИЊА" to "Frut i madh dhe i ëmbël vere me tul aromatik.",
 "ПИПЕРКА" to "Perime që mund të jetë e kuqe, e verdhë ose e gjelbër.",
 "КРОМИД" to "Perime që pritet shpesh gjatë gatimit dhe shkakton lot.",
 "ЛУК" to "Perime aromatike me erë të fortë, e përbërë nga thelpinj.",
 "СИРЕЊЕ" to "Produkt bulmeti që bëhet nga qumështi.",
 "ЛЕБ" to "Ushqim bazë që piqet me miell dhe ujë.",
 "МАЛИНА" to "Frut pylli i vogël, i kuq ose rozë.",
 "БОРОВИНКА" to "Frut pylli i vogël me ngjyrë blu të errët.",
 "КАПИНА" to "Frut i errët që rritet në një shkurre me gjemba.",
 "ПАПАЈА" to "Frut tropikal me tul portokalli dhe fara të zeza.",
 "МАНГО" to "Frut tropikal me tul të lëngshëm verdhë-portokalli.",
 "АВОКАДО" to "Frut i gjelbër me bërthamë të madhe dhe tul kremoz.",
 "ПЕЧУРКА" to "Kërpudhë e ngrënshme që përdoret në shumë gatime.",
 "ПЧЕНКА" to "Kokrra të verdha që rriten në kallinj.",
 "ТЕСТЕНИНИ" to "Ushqim i bërë nga brumi, shpesh në formë spageti.",
 "ЈОГУРТ" to "Produkt bulmeti me shije paksa të thartë."
)


private val frenchNatureClues = mapOf(
 "ПЛАНИНА" to "Grande élévation naturelle du terrain.",
 "СОНЦЕ" to "Étoile qui éclaire la Terre.",
 "РЕКА" to "Cours d’eau naturel qui s’écoule dans un lit.",
 "ВОДОПАД" to "Chute d’eau depuis une falaise ou une marche rocheuse.",
 "ЕЗЕРО" to "Grande étendue d’eau entourée de terres.",
 "ПЕШТЕРА" to "Cavité naturelle dans une roche ou une montagne.",
 "ВУЛКАН" to "Montagne qui peut laisser sortir de la lave.",
 "ОСТРОВ" to "Étendue de terre entourée d’eau de tous côtés.",
 "ШУМА" to "Grande étendue couverte de nombreux arbres.",
 "ЦВЕТ" to "Partie colorée d’une plante, souvent parfumée.",
 "ДРВО" to "Grande plante avec un tronc et des branches.",
 "ОБЛАК" to "Masse blanche ou grise de minuscules gouttelettes dans le ciel.",
 "ДОЖД" to "Eau qui tombe des nuages.",
 "СНЕГ" to "Cristaux de glace blancs qui tombent en hiver.",
 "ВЕТЕР" to "Mouvement de l’air.",
 "МОРЕ" to "Grande étendue d’eau salée.",
 "ПЕСОК" to "Petits grains que l’on trouve souvent sur les plages.",
 "ЅВЕЗДА" to "Corps céleste lumineux visible la nuit.",
 "ДОЛИНА" to "Terrain bas situé entre des collines ou des montagnes.",
 "РИД" to "Élévation du terrain moins haute qu’une montagne.",
 "КАНЈОН" to "Vallée profonde et étroite aux parois rocheuses abruptes.",
 "МОЛЊА" to "Puissant éclair électrique pendant un orage.",
 "ВИНОЖИТО" to "Arc coloré dans le ciel qui apparaît après la pluie.",
 "МАГЛА" to "Fines gouttelettes dans l’air qui réduisent la visibilité.",
 "ИЗВОР" to "Endroit où l’eau sort naturellement du sol.",
 "КРАЈБРЕЖЈЕ" to "Bande de terre située au bord de la mer ou d’un lac.",
 "ПУСТИНА" to "Région sèche avec très peu de pluie et de végétation.",
 "МОЧУРИШТЕ" to "Terrain humide avec de l’eau peu profonde et des plantes aquatiques.",
 "ВОДЕНА ЛИЛЈАНА" to "Plante aquatique aux grandes feuilles flottantes.",
 "КОРАЛЕН ГРЕБЕН" to "Structure sous-marine créée par les coraux.",
 "ГРОМ" to "Bruit puissant entendu pendant un orage.",
 "МРАЗ" to "Fine couche d’eau gelée sur une surface froide.",
 "СЕНКА" to "Zone sombre créée lorsqu’un objet bloque la lumière.",
 "ПРИЛИВ" to "Hausse du niveau de l’eau de mer.",
 "СЕВЕРНА СВЕТЛИНА" to "Lueurs colorées dans le ciel des régions polaires."
)
private val englishNatureClues = mapOf(
 "ПЛАНИНА" to "A high natural elevation of the land.",
 "СОНЦЕ" to "The star that lights up Earth.",
 "РЕКА" to "A natural flow of water moving through a channel.",
 "ВОДОПАД" to "Water falling from a high cliff or ledge.",
 "ЕЗЕРО" to "A large body of water surrounded by land.",
 "ПЕШТЕРА" to "A natural hollow in rock or a mountain.",
 "ВУЛКАН" to "A mountain that can erupt with lava.",
 "ОСТРОВ" to "Land surrounded by water on all sides.",
 "ШУМА" to "A large area covered with many trees.",
 "ЦВЕТ" to "The colorful part of a plant, often with a scent.",
 "ДРВО" to "A tall plant with a trunk and branches.",
 "ОБЛАК" to "A white or gray mass of tiny droplets in the sky.",
 "ДОЖД" to "Water falling from clouds.",
 "СНЕГ" to "White ice crystals that fall in winter.",
 "ВЕТЕР" to "The movement of air.",
 "МОРЕ" to "A large body of salt water.",
 "ПЕСОК" to "Tiny grains often found on beaches.",
 "ЅВЕЗДА" to "A bright celestial body visible at night.",
 "ДОЛИНА" to "Low land between hills or mountains.",
 "РИД" to "A raised area of land lower than a mountain.",
 "КАНЈОН" to "A deep, narrow valley with steep rocky sides.",
 "МОЛЊА" to "A powerful flash of electricity during a storm.",
 "ВИНОЖИТО" to "A colorful arc in the sky that appears after rain.",
 "МАГЛА" to "Tiny water droplets in the air that reduce visibility.",
 "ИЗВОР" to "A place where water naturally comes out of the ground.",
 "КРАЈБРЕЖЈЕ" to "The strip of land along a sea or lake.",
 "ПУСТИНА" to "A dry region with very little rain and few plants.",
 "МОЧУРИШТЕ" to "Wet land with shallow water and aquatic plants.",
 "ВОДЕНА ЛИЛЈАНА" to "An aquatic plant with large leaves floating on the surface.",
 "КОРАЛЕН ГРЕБЕН" to "An underwater structure formed by corals.",
 "ГРОМ" to "A loud sound heard during a thunderstorm.",
 "МРАЗ" to "A thin layer of frozen water on a cold surface.",
 "СЕНКА" to "A dark area made when something blocks light.",
 "ПРИЛИВ" to "A rise in the level of sea water.",
 "СЕВЕРНА СВЕТЛИНА" to "Colorful lights in the sky over polar regions."
)
private val albanianNatureClues = mapOf(
 "ПЛАНИНА" to "Lartësi e madhe natyrore e tokës.",
 "СОНЦЕ" to "Ylli që ndriçon Tokën.",
 "РЕКА" to "Rrjedhë natyrore uji që kalon në shtratin e saj.",
 "ВОДОПАД" to "Ujë që bie nga një shkëmb ose prag i lartë.",
 "ЕЗЕРО" to "Sipërfaqe e madhe uji e rrethuar nga toka.",
 "ПЕШТЕРА" to "Zgavër natyrore në shkëmb ose mal.",
 "ВУЛКАН" to "Mal nga i cili mund të shpërthejë llavë.",
 "ОСТРОВ" to "Tokë e rrethuar nga uji në të gjitha anët.",
 "ШУМА" to "Sipërfaqe e madhe e mbuluar me shumë pemë.",
 "ЦВЕТ" to "Pjesa me ngjyra e një bime, shpesh me aromë.",
 "ДРВО" to "Bimë e lartë me trung dhe degë.",
 "ОБЛАК" to "Masë e bardhë ose gri me pika të imëta uji në qiell.",
 "ДОЖД" to "Ujë që bie nga retë.",
 "СНЕГ" to "Kristale të bardha akulli që bien në dimër.",
 "ВЕТЕР" to "Lëvizja e ajrit.",
 "МОРЕ" to "Sipërfaqe e madhe me ujë të kripur.",
 "ПЕСОК" to "Kokrriza të imëta që gjenden shpesh në plazh.",
 "ЅВЕЗДА" to "Trup qiellor i ndritshëm që shihet natën.",
 "ДОЛИНА" to "Terren i ulët midis kodrave ose maleve.",
 "РИД" to "Pjesë e ngritur e tokës, më e ulët se mali.",
 "КАНЈОН" to "Luginë e thellë dhe e ngushtë me anë të pjerrëta shkëmbore.",
 "МОЛЊА" to "Shkëndijë e fuqishme elektrike gjatë stuhisë.",
 "ВИНОЖИТО" to "Hark shumëngjyrësh në qiell që shfaqet pas shiut.",
 "МАГЛА" to "Pika të imëta uji në ajër që ulin dukshmërinë.",
 "ИЗВОР" to "Vend ku uji del natyrshëm nga toka.",
 "КРАЈБРЕЖЈЕ" to "Rripi i tokës pranë detit ose liqenit.",
 "ПУСТИНА" to "Rajon i thatë me shumë pak shi dhe bimësi.",
 "МОЧУРИШТЕ" to "Tokë e lagësht me ujë të cekët dhe bimë ujore.",
 "ВОДЕНА ЛИЛЈАНА" to "Bimë ujore me gjethe të mëdha që pluskojnë në sipërfaqe.",
 "КОРАЛЕН ГРЕБЕН" to "Formacion nënujor i krijuar nga koralet.",
 "ГРОМ" to "Tingull i fortë që dëgjohet gjatë stuhisë.",
 "МРАЗ" to "Shtresë e hollë uji të ngrirë mbi një sipërfaqe të ftohtë.",
 "СЕНКА" to "Zonë e errët që krijohet kur diçka bllokon dritën.",
 "ПРИЛИВ" to "Ngritja e nivelit të ujit të detit.",
 "СЕВЕРНА СВЕТЛИНА" to "Drita shumëngjyrëshe në qiellin e rajoneve polare."
)


private val frenchTransportClues = mapOf(
 "АВТОМОБИЛ" to "Véhicule à quatre roues.",
 "ВЕЛОСИПЕД" to "Moyen de transport à deux roues et à pédales.",
 "ВОЗ" to "Moyen de transport qui circule sur des rails.",
 "МОТОЦИКЛ" to "Véhicule motorisé à deux roues.",
 "ТРАМВАЈ" to "Transport urbain qui circule sur des rails.",
 "ХЕЛИКОПТЕР" to "Aéronef doté de rotors pouvant décoller verticalement.",
 "ТРОТИНЕТ" to "Petit véhicule à deux roues avec une plateforme.",
 "КАМИОН" to "Grand véhicule motorisé destiné au transport de marchandises.",
 "АВИОН" to "Aéronef à ailes qui transporte des passagers.",
 "БРОД" to "Embarcation qui se déplace sur l’eau.",
 "АВТОБУС" to "Grand véhicule de transport public de passagers.",
 "ТАКСИ" to "Voiture qui transporte des passagers contre paiement.",
 "МЕТРО" to "Transport ferroviaire urbain souterrain.",
 "ЧАМЕЦ" to "Petite embarcation utilisée pour naviguer.",
 "ЕДРИЛИЦА" to "Bateau qui avance grâce au vent.",
 "СКУТЕР" to "Petit véhicule motorisé à deux roues.",
 "ПРИКОЛКА" to "Véhicule sans moteur tracté par un autre véhicule.",
 "БАГЕР" to "Engin de chantier qui creuse le sol.",
 "ТРОЛЕЈБУС" to "Véhicule électrique urbain alimenté par des câbles.",
 "ТРКАЛО" to "Pièce circulaire qui permet à un véhicule de rouler.",
 "ПОДМОРНИЦА" to "Embarcation capable de naviguer sous l’eau.",
 "ЖИЧНИЦА" to "Transport qui se déplace au-dessus du sol par câble.",
 "САНКА" to "Moyen de transport qui glisse sur la neige.",
 "КАРАВАН" to "Remorque ou véhicule aménagé pour voyager et séjourner.",
 "КОЧИЈА" to "Ancien moyen de transport tiré par des chevaux.",
 "БУЛДОЖЕР" to "Engin lourd qui pousse la terre et les débris.",
 "КОМБЕ" to "Petit véhicule motorisé pour transporter des personnes ou des marchandises.",
 "ТРАКТОР" to "Véhicule de travail utilisé surtout en agriculture.",
 "САМОЛЕТ" to "Aéronef doté de moteurs et d’ailes.",
 "ГЛИСЕР" to "Bateau à moteur rapide qui se déplace sur l’eau.",
 "ФЕРИБРОД" to "Grand bateau qui transporte des passagers et des voitures.",
 "ДИРИЖАБЛ" to "Aéronef plus léger que l’air avec une grande enveloppe remplie de gaz.",
 "ПАРЕН ВОЗ" to "Ancien train qui fonctionnait grâce à la vapeur.",
 "РОЛЕРИ" to "Chaussures à roulettes utilisées pour patiner.",
 "СКЕЈТБОРД" to "Planche à quatre roulettes sur laquelle on roule debout.",
 "СНЕГОМОБИЛ" to "Véhicule motorisé conçu pour circuler sur la neige."
)
private val englishTransportClues = mapOf(
 "АВТОМОБИЛ" to "A vehicle with four wheels.",
 "ВЕЛОСИПЕД" to "A means of transport with two wheels and pedals.",
 "ВОЗ" to "A means of transport that runs on rails.",
 "МОТОЦИКЛ" to "A motor vehicle with two wheels.",
 "ТРАМВАЈ" to "A city transport vehicle that runs on rails.",
 "ХЕЛИКОПТЕР" to "An aircraft with rotors that can take off vertically.",
 "ТРОТИНЕТ" to "A small two-wheeled vehicle with a standing platform.",
 "КАМИОН" to "A large motor vehicle used to carry goods.",
 "АВИОН" to "An aircraft with wings that carries passengers.",
 "БРОД" to "A vessel that travels on water.",
 "АВТОБУС" to "A large passenger vehicle used for public transport.",
 "ТАКСИ" to "A car that carries passengers for a fare.",
 "МЕТРО" to "An underground urban rail transport system.",
 "ЧАМЕЦ" to "A small vessel used for travelling on water.",
 "ЕДРИЛИЦА" to "A boat that moves with the help of the wind.",
 "СКУТЕР" to "A small motor vehicle with two wheels.",
 "ПРИКОЛКА" to "A vehicle without an engine pulled by another vehicle.",
 "БАГЕР" to "A construction machine used for digging soil.",
 "ТРОЛЕЈБУС" to "An electric city vehicle powered through overhead wires.",
 "ТРКАЛО" to "A circular part that helps a vehicle move.",
 "ПОДМОРНИЦА" to "A vessel that can travel beneath the water's surface.",
 "ЖИЧНИЦА" to "A transport system that moves above the ground on a cable.",
 "САНКА" to "A vehicle that slides over snow.",
 "КАРАВАН" to "A trailer or vehicle equipped for travelling and staying overnight.",
 "КОЧИЈА" to "An old means of transport pulled by horses.",
 "БУЛДОЖЕР" to "A heavy machine that pushes soil and rubble.",
 "КОМБЕ" to "A small motor vehicle for carrying people or goods.",
 "ТРАКТОР" to "A work vehicle used mainly in agriculture.",
 "САМОЛЕТ" to "An aircraft with engines and wings.",
 "ГЛИСЕР" to "A fast motorboat that travels on water.",
 "ФЕРИБРОД" to "A large ship that carries passengers and cars.",
 "ДИРИЖАБЛ" to "An aircraft lighter than air with a large gas-filled envelope.",
 "ПАРЕН ВОЗ" to "An old type of train powered by steam.",
 "РОЛЕРИ" to "Shoes with wheels used for skating.",
 "СКЕЈТБОРД" to "A board with four wheels that you ride standing up.",
 "СНЕГОМОБИЛ" to "A motor vehicle designed to travel over snow."
)
private val albanianTransportClues = mapOf(
 "АВТОМОБИЛ" to "Mjet me katër rrota.",
 "ВЕЛОСИПЕД" to "Mjet transporti me dy rrota dhe pedale.",
 "ВОЗ" to "Mjet transporti që lëviz mbi shina.",
 "МОТОЦИКЛ" to "Mjet motorik me dy rrota.",
 "ТРАМВАЈ" to "Mjet transporti urban që lëviz mbi shina.",
 "ХЕЛИКОПТЕР" to "Mjet fluturues me rotorë që mund të ngrihet vertikalisht.",
 "ТРОТИНЕТ" to "Mjet i vogël me dy rrota dhe platformë për këmbët.",
 "КАМИОН" to "Mjet i madh motorik për transportin e mallrave.",
 "АВИОН" to "Mjet fluturues me krahë që transporton pasagjerë.",
 "БРОД" to "Mjet lundrues që lëviz në ujë.",
 "АВТОБУС" to "Mjet i madh për transportin publik të pasagjerëve.",
 "ТАКСИ" to "Automjet që transporton pasagjerë kundrejt pagesës.",
 "МЕТРО" to "Transport hekurudhor urban nëntokësor.",
 "ЧАМЕЦ" to "Mjet i vogël lundrues për të lëvizur në ujë.",
 "ЕДРИЛИЦА" to "Anije që lëviz me ndihmën e erës.",
 "СКУТЕР" to "Mjet i vogël motorik me dy rrota.",
 "ПРИКОЛКА" to "Mjet pa motor që tërhiqet nga një mjet tjetër.",
 "БАГЕР" to "Makinë pune që gërmon dheun.",
 "ТРОЛЕЈБУС" to "Mjet elektrik urban që furnizohet me energji përmes telave.",
 "ТРКАЛО" to "Pjesë e rrumbullakët që ndihmon mjetin të lëvizë.",
 "ПОДМОРНИЦА" to "Mjet lundrues që mund të lëvizë nën sipërfaqen e ujit.",
 "ЖИЧНИЦА" to "Mjet transporti që lëviz lart mbi tokë me kabllo.",
 "САНКА" to "Mjet transporti që rrëshqet mbi borë.",
 "КАРАВАН" to "Rimorkio ose mjet i pajisur për udhëtim dhe qëndrim.",
 "КОЧИЈА" to "Mjet i vjetër transporti që tërhiqet nga kuajt.",
 "БУЛДОЖЕР" to "Makinë e rëndë që shtyn dheun dhe rrënojat.",
 "КОМБЕ" to "Mjet i vogël motorik për transportin e njerëzve ose mallrave.",
 "ТРАКТОР" to "Mjet pune që përdoret kryesisht në bujqësi.",
 "САМОЛЕТ" to "Mjet fluturues me motorë dhe krahë.",
 "ГЛИСЕР" to "Varkë e shpejtë me motor që lëviz në ujë.",
 "ФЕРИБРОД" to "Anije e madhe që transporton pasagjerë dhe makina.",
 "ДИРИЖАБЛ" to "Mjet fluturues më i lehtë se ajri me një mbështjellës të madh të mbushur me gaz.",
 "ПАРЕН ВОЗ" to "Lloj i vjetër treni që lëvizte me avull.",
 "РОЛЕРИ" to "Këpucë me rrota që përdoren për patinazh.",
 "СКЕЈТБОРД" to "Dërrasë me katër rrota mbi të cilën lëviz duke qëndruar në këmbë.",
 "СНЕГОМОБИЛ" to "Mjet motorik i projektuar për të lëvizur mbi borë."
)


private val frenchSportClues = mapOf(
 "ФУДБАЛ" to "Sport qui se joue avec un ballon et les pieds.",
 "КОШАРКА" to "Sport où l’on lance un ballon dans un panier.",
 "ПЛИВАЊЕ" to "Activité sportive pratiquée dans l’eau.",
 "ОДБОЈКА" to "Sport où deux équipes font passer un ballon au-dessus d’un filet.",
 "СКИЈАЊЕ" to "Sport d’hiver qui consiste à glisser sur la neige avec des skis.",
 "ВЕЛОСИПЕДИЗАМ" to "Sport qui consiste à faire du vélo.",
 "АТЛЕТИКА" to "Sport comprenant la course, le saut et le lancer.",
 "ГИМНАСТИКА" to "Sport composé d’exercices de force, d’équilibre et de souplesse.",
 "ТЕНИС" to "Sport joué avec une raquette et une balle au-dessus d’un filet.",
 "БОКС" to "Sport de combat où l’on porte des coups avec des gants.",
 "РАКОМЕТ" to "Sport collectif où l’on lance un ballon avec la main.",
 "БЕЈЗБОЛ" to "Sport avec une batte, une balle et des bases.",
 "ГОЛФ" to "Sport où l’on frappe une balle vers un trou avec un club.",
 "КАРАТЕ" to "Art martial avec des coups et des blocages.",
 "ЏУДО" to "Sport de combat avec des projections et des prises.",
 "ЈАВАЊЕ" to "Activité sportive pratiquée à cheval.",
 "КАЈАК" to "Sport de pagaie pratiqué dans une embarcation étroite.",
 "КЛИЗГАЊЕ" to "Déplacement sur la glace avec des patins.",
 "ПИНГ-ПОНГ" to "Jeu avec de petites raquettes et une balle sur une table.",
 "КУГЛАЊЕ" to "Sport où l’on renverse des quilles avec une boule lourde.",
 "СТРЕЛАШТВО" to "Sport qui consiste à atteindre une cible avec un arc ou une arme.",
 "ФЕХТУВАЊЕ" to "Sport de combat avec des épées et un équipement de protection.",
 "РЕГБИ" to "Sport collectif joué avec un ballon ovale.",
 "КРИКЕТ" to "Sport collectif avec une batte et une balle, populaire dans de nombreux pays.",
 "СНОУБОРД" to "Sport d’hiver pratiqué avec une seule planche sur la neige.",
 "СУРФАЊЕ" to "Sport qui consiste à glisser sur les vagues avec une planche.",
 "МАРАТОН" to "Course de longue distance de 42 kilomètres.",
 "КАРЛИНГ" to "Sport d’équipe d’hiver où des pierres glissent sur la glace.",
 "ФУТСАЛ" to "Variante du football jouée en salle avec une équipe réduite.",
 "ВЕСЛАЊЕ" to "Sport où les athlètes propulsent un bateau avec des rames.",
 "ТРИАТЛОН" to "Épreuve qui combine natation, cyclisme et course à pied.",
 "ПАРКУР" to "Activité sportive qui consiste à franchir des obstacles en ville.",
 "КАЧУВАЊЕ" to "Sport qui consiste à escalader des rochers ou des murs artificiels.",
 "ДИГАЊЕ ТЕГОВИ" to "Sport où les participants soulèvent des poids lourds.",
 "БОРЕЊЕ" to "Sport de combat où deux adversaires tentent de prendre le dessus.",
 "БАДМИНТОН" to "Sport de raquette où l’on frappe un volant.",
 "СЕПАКТАКРАВ" to "Sport collectif d’Asie du Sud-Est où l’on frappe un ballon avec les pieds.",
 "СОФТБОЛ" to "Sport collectif proche du baseball, joué avec une balle plus grosse."
)
private val englishSportClues = mapOf(
 "ФУДБАЛ" to "A sport played with a ball and the feet.",
 "КОШАРКА" to "A sport in which players throw a ball into a basket.",
 "ПЛИВАЊЕ" to "A sporting activity performed in water.",
 "ОДБОЈКА" to "A sport where two teams hit a ball over a net.",
 "СКИЈАЊЕ" to "A winter sport involving sliding over snow on skis.",
 "ВЕЛОСИПЕДИЗАМ" to "The sport of riding bicycles.",
 "АТЛЕТИКА" to "A sport involving running, jumping and throwing.",
 "ГИМНАСТИКА" to "A sport involving strength, balance and flexibility exercises.",
 "ТЕНИС" to "A sport played with a racket and ball over a net.",
 "БОКС" to "A combat sport in which punches are thrown while wearing gloves.",
 "РАКОМЕТ" to "A team sport in which players throw a ball by hand.",
 "БЕЈЗБОЛ" to "A sport played with a bat, ball and bases.",
 "ГОЛФ" to "A sport in which a club is used to hit a ball into a hole.",
 "КАРАТЕ" to "A martial art involving strikes and blocks.",
 "ЏУДО" to "A combat sport involving throws and holds.",
 "ЈАВАЊЕ" to "A sporting activity performed on horseback.",
 "КАЈАК" to "A paddling sport using a narrow boat.",
 "КЛИЗГАЊЕ" to "Moving over ice using skates.",
 "ПИНГ-ПОНГ" to "A game played with small bats and a ball on a table.",
 "КУГЛАЊЕ" to "A sport in which a heavy ball knocks down pins.",
 "СТРЕЛАШТВО" to "A sport in which a target is hit with a bow or weapon.",
 "ФЕХТУВАЊЕ" to "A combat sport using swords and protective equipment.",
 "РЕГБИ" to "A team sport played with an oval ball.",
 "КРИКЕТ" to "A team sport played with a bat and ball, popular in many countries.",
 "СНОУБОРД" to "A winter sport using one wide board on snow.",
 "СУРФАЊЕ" to "A sport in which a board is ridden on waves.",
 "МАРАТОН" to "A long-distance running race of 42 kilometres.",
 "КАРЛИНГ" to "A winter team sport in which stones slide across ice.",
 "ФУТСАЛ" to "A version of football played indoors with smaller teams.",
 "ВЕСЛАЊЕ" to "A sport in which athletes move a boat using oars.",
 "ТРИАТЛОН" to "A competition combining swimming, cycling and running.",
 "ПАРКУР" to "A sport involving jumping over obstacles in an urban area.",
 "КАЧУВАЊЕ" to "A sport involving climbing rocks or artificial walls.",
 "ДИГАЊЕ ТЕГОВИ" to "A sport in which competitors lift heavy weights.",
 "БОРЕЊЕ" to "A combat sport in which two competitors try to overpower each other.",
 "БАДМИНТОН" to "A racket sport in which players hit a shuttlecock.",
 "СЕПАКТАКРАВ" to "A Southeast Asian team sport in which a ball is kicked with the feet.",
 "СОФТБОЛ" to "A team sport similar to baseball, played with a larger ball."
)
private val albanianSportClues = mapOf(
 "ФУДБАЛ" to "Sport që luhet me top dhe këmbë.",
 "КОШАРКА" to "Sport ku topi hidhet në kosh.",
 "ПЛИВАЊЕ" to "Aktivitet sportiv që zhvillohet në ujë.",
 "ОДБОЈКА" to "Sport ku dy ekipe kalojnë topin mbi rrjetë.",
 "СКИЈАЊЕ" to "Sport dimëror ku rrëshqitet mbi borë me ski.",
 "ВЕЛОСИПЕДИЗАМ" to "Sporti i ngasjes së biçikletës.",
 "АТЛЕТИКА" to "Sport që përfshin vrapimin, kërcimin dhe hedhjen.",
 "ГИМНАСТИКА" to "Sport me ushtrime për forcë, ekuilibër dhe fleksibilitet.",
 "ТЕНИС" to "Sport që luhet me raketë dhe top mbi rrjetë.",
 "БОКС" to "Sport luftarak ku goditet me doreza boksi.",
 "РАКОМЕТ" to "Sport ekipor ku topi hidhet me dorë.",
 "БЕЈЗБОЛ" to "Sport me shkop, top dhe baza.",
 "ГОЛФ" to "Sport ku topi goditet me shkop drejt një vrime.",
 "КАРАТЕ" to "Art luftarak me goditje dhe bllokime.",
 "ЏУДО" to "Sport luftarak me hedhje dhe kapje.",
 "ЈАВАЊЕ" to "Aktivitet sportiv mbi kalë.",
 "КАЈАК" to "Sport me vozitje në një varkë të ngushtë.",
 "КЛИЗГАЊЕ" to "Lëvizje mbi akull me patina.",
 "ПИНГ-ПОНГ" to "Lojë me raketa të vogla dhe top mbi tavolinë.",
 "КУГЛАЊЕ" to "Sport ku rrëzohen birilat me një top të rëndë.",
 "СТРЕЛАШТВО" to "Sport ku goditet një shenjë me hark ose armë.",
 "ФЕХТУВАЊЕ" to "Sport luftarak me shpata dhe pajisje mbrojtëse.",
 "РЕГБИ" to "Sport ekipor që luhet me top oval.",
 "КРИКЕТ" to "Sport ekipor me shkop dhe top, i përhapur në shumë vende.",
 "СНОУБОРД" to "Sport dimëror me një dërrasë të gjerë mbi borë.",
 "СУРФАЊЕ" to "Sport ku lëvizet mbi valë me dërrasë.",
 "МАРАТОН" to "Garë vrapimi në distancë të gjatë prej 42 kilometrash.",
 "КАРЛИНГ" to "Sport ekipor dimëror ku gurët rrëshqasin mbi akull.",
 "ФУТСАЛ" to "Variant i futbollit që luhet në sallë me ekip më të vogël.",
 "ВЕСЛАЊЕ" to "Sport ku atletët lëvizin varkën me lopata.",
 "ТРИАТЛОН" to "Garë që bashkon notin, çiklizmin dhe vrapimin.",
 "ПАРКУР" to "Aktivitet sportiv me kapërcim pengesash në mjedis urban.",
 "КАЧУВАЊЕ" to "Sport ku ngjiten shkëmbinj ose mure artificiale.",
 "ДИГАЊЕ ТЕГОВИ" to "Sport ku garuesit ngrenë pesha të rënda.",
 "БОРЕЊЕ" to "Sport luftarak ku dy garues përpiqen të mposhtin njëri-tjetrin.",
 "БАДМИНТОН" to "Sport me raketa ku goditet një puplë.",
 "СЕПАКТАКРАВ" to "Sport ekipor i Azisë Juglindore ku topi goditet me këmbë.",
 "СОФТБОЛ" to "Sport ekipor i ngjashëm me bejsbollin, me top më të madh."
)


private val frenchPlaceClues = mapOf(
 "УЧИЛИШТЕ" to "Lieu où les enfants apprennent.",
 "БИБЛИОТЕКА" to "Lieu où l’on emprunte des livres.",
 "БОЛНИЦА" to "Lieu où les médecins soignent les personnes malades.",
 "ТЕАТАР" to "Bâtiment où des spectacles sont présentés.",
 "СТАДИОН" to "Grand espace où se déroulent des compétitions sportives.",
 "АЕРОДРОМ" to "Lieu où les avions décollent et atterrissent.",
 "ЖЕЛЕЗНИЧКА СТАНИЦА" to "Lieu où les trains s’arrêtent et partent.",
 "ПАЗАР" to "Lieu où l’on achète des aliments frais et d’autres produits.",
 "МУЗЕЈ" to "Lieu où sont exposés des objets historiques et artistiques.",
 "ПЛАЖА" to "Lieu au bord de la mer ou d’un lac, avec du sable ou des galets.",
 "ЦРКВА" to "Édifice religieux où se déroulent des cérémonies chrétiennes.",
 "ЏАМИЈА" to "Édifice religieux où les musulmans prient.",
 "РЕСТОРАН" to "Lieu où l’on prépare et sert des repas.",
 "ХОТЕЛ" to "Lieu où les voyageurs séjournent et dorment.",
 "БАНКА" to "Lieu où l’argent est conservé et géré.",
 "ПОШТА" to "Lieu où l’on envoie des lettres et des colis.",
 "ПАРК" to "Espace vert public pour se promener et se reposer.",
 "ФАРМА" to "Lieu où l’on élève des animaux ou cultive des plantes.",
 "БЕНЗИНСКА ПУМПА" to "Lieu où les véhicules font le plein de carburant.",
 "АПТЕКА" to "Lieu où l’on achète des médicaments et des produits médicaux.",
 "ПЕКАРНИЦА" to "Magasin où l’on vend du pain frais et des viennoiseries.",
 "СУПЕРМАРКЕТ" to "Grand magasin qui vend de la nourriture et des produits courants.",
 "ПОЖАРНА СТАНИЦА" to "Lieu d’où les pompiers partent en intervention.",
 "ПОЛИЦИСКА СТАНИЦА" to "Lieu où travaille la police.",
 "ГРАДИНКА" to "Lieu où les jeunes enfants sont accueillis et apprennent.",
 "АВТОБУСКА СТАНИЦА" to "Lieu où les autobus arrivent et partent.",
 "ЗООЛОШКА ГРАДИНА" to "Lieu où les visiteurs observent différents animaux.",
 "БАЗЕН" to "Lieu rempli d’eau où l’on peut nager.",
 "ОПШТИНА" to "Institution publique qui gère les affaires locales d’une commune.",
 "СУД" to "Institution où les juges tranchent les litiges juridiques.",
 "ЗАТВОР" to "Établissement où sont détenues les personnes condamnées à la prison.",
 "УНИВЕРЗИТЕТ" to "Établissement d’enseignement supérieur où l’on fait des études.",
 "ЛАБОРАТОРИЈА" to "Pièce où sont menées des recherches et des expériences scientifiques.",
 "ОПЕРСКА КУЌА" to "Bâtiment où sont présentés des spectacles d’opéra.",
 "КИНО" to "Lieu où l’on projette des films sur grand écran.",
 "ЗАБАВЕН ПАРК" to "Lieu avec des attractions, des manèges et des toboggans.",
 "ПРИСТАНИШТЕ" to "Lieu au bord de l’eau où les bateaux accostent.",
 "СВЕТИЛНИК" to "Tour élevée au bord de la mer qui aide les navires à s’orienter."
)
private val englishPlaceClues = mapOf(
 "УЧИЛИШТЕ" to "A place where children learn.",
 "БИБЛИОТЕКА" to "A place where people borrow books.",
 "БОЛНИЦА" to "A place where doctors treat sick people.",
 "ТЕАТАР" to "A building where performances are held.",
 "СТАДИОН" to "A large place where sports competitions take place.",
 "АЕРОДРОМ" to "A place where planes take off and land.",
 "ЖЕЛЕЗНИЧКА СТАНИЦА" to "A place where trains stop and depart.",
 "ПАЗАР" to "A place where people buy fresh food and other products.",
 "МУЗЕЈ" to "A place where historical and artistic objects are displayed.",
 "ПЛАЖА" to "A place by the sea or a lake with sand or pebbles.",
 "ЦРКВА" to "A religious building where Christian services are held.",
 "ЏАМИЈА" to "A religious building where Muslims pray.",
 "РЕСТОРАН" to "A place where food is prepared and served.",
 "ХОТЕЛ" to "A place where travellers stay and sleep.",
 "БАНКА" to "A place where money is kept and managed.",
 "ПОШТА" to "A place where letters and parcels are sent.",
 "ПАРК" to "A public green space for walking and relaxing.",
 "ФАРМА" to "A place where animals or plants are raised.",
 "БЕНЗИНСКА ПУМПА" to "A place where vehicles refuel.",
 "АПТЕКА" to "A place where medicines and medical products are sold.",
 "ПЕКАРНИЦА" to "A shop where fresh bread and pastries are sold.",
 "СУПЕРМАРКЕТ" to "A large shop selling food and everyday products.",
 "ПОЖАРНА СТАНИЦА" to "A place from which firefighters respond to emergencies.",
 "ПОЛИЦИСКА СТАНИЦА" to "A place where the police work.",
 "ГРАДИНКА" to "A place where young children are cared for and learn.",
 "АВТОБУСКА СТАНИЦА" to "A place where buses arrive and depart.",
 "ЗООЛОШКА ГРАДИНА" to "A place where visitors can see different animals.",
 "БАЗЕН" to "A place filled with water for swimming.",
 "ОПШТИНА" to "A public institution that manages local municipal affairs.",
 "СУД" to "An institution where judges settle legal disputes.",
 "ЗАТВОР" to "A facility where people sentenced to prison are held.",
 "УНИВЕРЗИТЕТ" to "A higher education institution where people study.",
 "ЛАБОРАТОРИЈА" to "A room where scientific research and experiments are carried out.",
 "ОПЕРСКА КУЌА" to "A building where opera performances are held.",
 "КИНО" to "A place where films are shown on a big screen.",
 "ЗАБАВЕН ПАРК" to "A place with rides, attractions and slides.",
 "ПРИСТАНИШТЕ" to "A place by the water where ships dock.",
 "СВЕТИЛНИК" to "A tall tower by the sea that helps ships navigate."
)
private val albanianPlaceClues = mapOf(
 "УЧИЛИШТЕ" to "Vend ku fëmijët mësojnë.",
 "БИБЛИОТЕКА" to "Vend ku njerëzit marrin libra hua.",
 "БОЛНИЦА" to "Vend ku mjekët trajtojnë njerëzit e sëmurë.",
 "ТЕАТАР" to "Ndërtesë ku zhvillohen shfaqje.",
 "СТАДИОН" to "Hapësirë e madhe ku zhvillohen gara sportive.",
 "АЕРОДРОМ" to "Vend ku avionët ngrihen dhe ulen.",
 "ЖЕЛЕЗНИЧКА СТАНИЦА" to "Vend ku trenat ndalojnë dhe nisen.",
 "ПАЗАР" to "Vend ku njerëzit blejnë ushqime të freskëta dhe produkte të tjera.",
 "МУЗЕЈ" to "Vend ku ekspozohen objekte historike dhe artistike.",
 "ПЛАЖА" to "Vend pranë detit ose liqenit me rërë ose gurë të vegjël.",
 "ЦРКВА" to "Ndërtesë fetare ku mbahen shërbesat e krishtera.",
 "ЏАМИЈА" to "Ndërtesë fetare ku falen myslimanët.",
 "РЕСТОРАН" to "Vend ku përgatitet dhe shërbehet ushqimi.",
 "ХОТЕЛ" to "Vend ku udhëtarët qëndrojnë dhe flenë.",
 "БАНКА" to "Vend ku ruhen dhe administrohen paratë.",
 "ПОШТА" to "Vend ku dërgohen letra dhe pako.",
 "ПАРК" to "Hapësirë publike e gjelbër për shëtitje dhe pushim.",
 "ФАРМА" to "Vend ku rriten kafshë ose bimë.",
 "БЕНЗИНСКА ПУМПА" to "Vend ku automjetet mbushen me karburant.",
 "АПТЕКА" to "Vend ku shiten ilaçe dhe produkte mjekësore.",
 "ПЕКАРНИЦА" to "Dyqan ku shitet bukë e freskët dhe produkte furre.",
 "СУПЕРМАРКЕТ" to "Dyqan i madh që shet ushqime dhe produkte të përditshme.",
 "ПОЖАРНА СТАНИЦА" to "Vend nga ku zjarrfikësit nisen për ndërhyrje.",
 "ПОЛИЦИСКА СТАНИЦА" to "Vend ku punon policia.",
 "ГРАДИНКА" to "Vend ku kujdesen dhe mësojnë fëmijët e vegjël.",
 "АВТОБУСКА СТАНИЦА" to "Vend ku mbërrijnë dhe nisen autobusët.",
 "ЗООЛОШКА ГРАДИНА" to "Vend ku vizitorët shohin kafshë të ndryshme.",
 "БАЗЕН" to "Vend i mbushur me ujë për not.",
 "ОПШТИНА" to "Institucion publik që organizon çështjet lokale të një komune.",
 "СУД" to "Institucion ku gjyqtarët zgjidhin mosmarrëveshjet ligjore.",
 "ЗАТВОР" to "Institucion ku mbahen personat e dënuar me burg.",
 "УНИВЕРЗИТЕТ" to "Institucion i arsimit të lartë ku studiohet.",
 "ЛАБОРАТОРИЈА" to "Dhoma ku kryhen kërkime dhe eksperimente shkencore.",
 "ОПЕРСКА КУЌА" to "Ndërtesë ku shfaqen opera.",
 "КИНО" to "Vend ku shfaqen filma në ekran të madh.",
 "ЗАБАВЕН ПАРК" to "Vend me atraksione, lodra rrotulluese dhe rrëshqitëse.",
 "ПРИСТАНИШТЕ" to "Vend pranë ujit ku ankorohen anijet.",
 "СВЕТИЛНИК" to "Kullë e lartë pranë detit që ndihmon anijet të orientohen."
)

private fun translatedPuzzleWord(word:String,lang:String):String =
 when(lang) { "fr" -> frenchAnimalWords[word] ?: frenchFoodWords[word] ?: frenchOtherWords[word] ?: word; "en" -> englishWords[word] ?: word; "sq" -> albanianWords[word] ?: word; else -> word }
private fun translatedPuzzleClue(word:String,clue:String,lang:String):String =
 when(lang) {
  "fr" -> frenchAnimalClues[word] ?: frenchFoodClues[word] ?: frenchNatureClues[word] ?: frenchTransportClues[word] ?: frenchSportClues[word] ?: frenchPlaceClues[word] ?: clue
  "en" -> englishClues[word] ?: englishFoodClues[word] ?: englishNatureClues[word] ?: englishTransportClues[word] ?: englishSportClues[word] ?: englishPlaceClues[word] ?: clue
  "sq" -> albanianClues[word] ?: albanianFoodClues[word] ?: albanianNatureClues[word] ?: albanianTransportClues[word] ?: albanianSportClues[word] ?: albanianPlaceClues[word] ?: clue
  else -> clue
 }
private fun translatedCategory(title:String,lang:String):String {
 val translations=mapOf(
  "ЖИВОТНИ" to listOf("ANIMAUX","KAFSHËT","ANIMALS"),
  "ОВОШЈЕ И ХРАНА" to listOf("FRUITS ET ALIMENTS","FRUTA DHE USHQIME","FRUIT AND FOOD"),
  "ОВОШЈЕ" to listOf("FRUITS","FRUTA","FRUIT"),
  "ХРАНА" to listOf("ALIMENTS","USHQIME","FOOD"),
  "ПРИРОДА" to listOf("NATURE","NATYRË","NATURE"),
  "ПРЕВОЗ" to listOf("TRANSPORTS","TRANSPORT","TRANSPORT"),
  "СПОРТ" to listOf("SPORTS","SPORT","SPORTS"),
  "МЕСТА" to listOf("LIEUX","VENDE","PLACES")
 )
 val i=when(lang){"fr"->0;"sq"->1;"en"->2;else->-1}
 return if(i<0) title else translations[title]?.get(i) ?: title
}
data class Puzzle(val word:String,val clue:String,val category:String,val photo:String)
data class GameCategory(val title:String,val emoji:String,val puzzles:List<Puzzle>)
val categories = listOf(
 GameCategory("ЖИВОТНИ","🐾", listOf(
  Puzzle("КУЧЕ","Домашно животно што лае.","ЖИВОТНИ","https://images.unsplash.com/photo-1552053831-71594a27632d?w=900"),
  Puzzle("МАЧКА","Домашно животно што мјаука.","ЖИВОТНИ","https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=900"),
  Puzzle("ЛАВ","Голема дива мачка што живее во Африка.","ЖИВОТНИ","https://images.unsplash.com/photo-1516026672322-bc52d61a55d5?w=900"),
  Puzzle("ЗЕБРА","Африканско животно со црно-бели линии.","ЖИВОТНИ","https://images.unsplash.com/photo-1501706362039-c6e08d6c6f1b?w=900"),
  Puzzle("МАЈМУН","Животно што се качува по дрвја.","ЖИВОТНИ","https://images.unsplash.com/photo-1540573133985-87b6da6d54a9?w=900"),
  Puzzle("МЕЧКА","Големо шумско животно што оди на четири нозе.","ЖИВОТНИ","https://images.unsplash.com/photo-1530595467537-0b5996c41f2d?w=900"),
  Puzzle("ВОЛК","Диво животно слично на куче што живее во глутница.","ЖИВОТНИ","https://images.unsplash.com/photo-1564466809058-bf4114d55352?w=900"),
  Puzzle("ДЕЛФИН","Паметно морско животно што плива и скока.","ЖИВОТНИ","https://images.unsplash.com/photo-1607153333879-c174d265f1d2?w=900"),
  Puzzle("ТИГАР","Голема дива мачка со портокалово крзно и црни линии.","ЖИВОТНИ","https://loremflickr.com/900/600/tiger?lock=1000"),
  Puzzle("ЛИСИЦА","Диво животно со бушава опашка и остар муцун.","ЖИВОТНИ","https://loremflickr.com/900/600/fox?lock=1001"),
  Puzzle("ЗАЈАК","Мало животно со долги уши што скока.","ЖИВОТНИ","https://loremflickr.com/900/600/rabbit?lock=1002"),
  Puzzle("КОЊ","Големо домашно животно на кое може да се јава.","ЖИВОТНИ","https://loremflickr.com/900/600/horse?lock=1003"),
  Puzzle("КРАВА","Домашно животно што дава млеко.","ЖИВОТНИ","https://loremflickr.com/900/600/cow?lock=1004"),
  Puzzle("ОВЦА","Домашно животно со волнено крзно.","ЖИВОТНИ","https://loremflickr.com/900/600/sheep?lock=1005"),
  Puzzle("КОЗА","Домашно животно што се качува по карпи.","ЖИВОТНИ","https://loremflickr.com/900/600/goat?lock=1006"),
  Puzzle("ЖЕЛКА","Животно со тврд оклоп на грбот.","ЖИВОТНИ","https://loremflickr.com/900/600/turtle?lock=1007"),
  Puzzle("ПАПАГАЛ","Шарена птица што може да имитира звуци.","ЖИВОТНИ","https://loremflickr.com/900/600/parrot?lock=1008"),
  Puzzle("ОРЕЛ","Голема граблива птица со силни канџи.","ЖИВОТНИ","https://loremflickr.com/900/600/eagle?lock=1009"),
  Puzzle("СЛОН","Големо животно со долга сурла и големи уши.","ЖИВОТНИ","https://loremflickr.com/900/600/elephant?lock=11215"),
  Puzzle("ЖИРАФА","Високо африканско животно со долг врат.","ЖИВОТНИ","https://loremflickr.com/900/600/giraffe?lock=13290"),
  Puzzle("НОСОРОГ","Големо животно со дебела кожа и рог на носот.","ЖИВОТНИ","https://loremflickr.com/900/600/rhinoceros?lock=14371"),
  Puzzle("НИЛСКИ КОЊ","Големо африканско животно што живее во вода и покрај неа.","ЖИВОТНИ","https://loremflickr.com/900/600/hippopotamus?lock=16477"),
  Puzzle("КЕНГУР","Австралиско животно што скока и го носи младенчето во торба.","ЖИВОТНИ","https://loremflickr.com/900/600/kangaroo?lock=13306"),
  Puzzle("ПИНГВИН","Птица што не лета, но одлично плива во студени мориња.","ЖИВОТНИ","https://loremflickr.com/900/600/penguin?lock=14342"),
  Puzzle("БУВ","Ноќна птица со големи очи.","ЖИВОТНИ","https://loremflickr.com/900/600/owl?lock=10142"),
  Puzzle("ПАУН","Птица позната по големата шарена опашка.","ЖИВОТНИ","https://loremflickr.com/900/600/peacock?lock=11207"),
  Puzzle("ЕЖ","Мало животно покриено со боцки.","ЖИВОТНИ","https://loremflickr.com/900/600/hedgehog?lock=9091"),
  Puzzle("ВЕРВЕРИЦА","Мало животно што се качува по дрвја и собира ореви.","ЖИВОТНИ","https://loremflickr.com/900/600/squirrel?lock=16436"),
  Puzzle("КАМИЛА","Животно со грпка што може долго да издржи без вода.","ЖИВОТНИ","https://loremflickr.com/900/600/camel?lock=33281"),
  Puzzle("ПАНДА","Црно-бело мече што најчесто јаде бамбус.","ЖИВОТНИ","https://loremflickr.com/900/600/giant%20panda?lock=32232"),
  Puzzle("КОАЛА","Австралиско торбарско животно што живее на дрвја.","ЖИВОТНИ","https://loremflickr.com/900/600/koala?lock=32235"),
  Puzzle("КРОКОДИЛ","Голем влекач со силни вилици што живее во вода и на копно.","ЖИВОТНИ","https://loremflickr.com/900/600/crocodile?lock=35407"),
  Puzzle("ЗМИЈА","Влекач без нозе што се движи ползејќи.","ЖИВОТНИ","https://loremflickr.com/900/600/snake?lock=32219"),
  Puzzle("ЖАБА","Мало водоземно животно што скока и квака.","ЖИВОТНИ","https://loremflickr.com/900/600/frog?lock=31167"),
  Puzzle("ПЧЕЛА","Инсект што собира нектар и произведува мед.","ЖИВОТНИ","https://loremflickr.com/900/600/honey%20bee?lock=32254"),
  Puzzle("ПЕПЕРУТКА","Инсект со големи шарени крилја.","ЖИВОТНИ","https://loremflickr.com/900/600/butterfly?lock=36463"),
  Puzzle("МИШКА","Мало глодарче со долга опашка.","ЖИВОТНИ","https://loremflickr.com/900/600/mouse?lock=32254"),
  Puzzle("СВИЊА","Домашно животно што често живее на фарма и рие со муцката.","ЖИВОТНИ","https://loremflickr.com/900/600/pig?lock=32221")
 )),
 GameCategory("ОВОШЈЕ И ХРАНА","🍎", listOf(
  Puzzle("ЈАБОЛКО","Црвено или зелено овошје што расте на дрво.","ОВОШЈЕ","https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=900"),
  Puzzle("БАНАНА","Жолто овошје што го лупиме пред јадење.","ОВОШЈЕ","https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=900"),
  Puzzle("ПОРТОКАЛ","Тркалесто цитрусно овошје со портокалова кора.","ОВОШЈЕ","https://images.unsplash.com/photo-1547514701-42782101795e?w=900"),
  Puzzle("ЈАГОДА","Црвено овошје со мали семки на површината.","ОВОШЈЕ","https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=900"),
  Puzzle("ЛИМОН","Жолто кисело цитрусно овошје.","ОВОШЈЕ","https://images.unsplash.com/photo-1590502593747-42a996133562?w=900"),
  Puzzle("ЛУБЕНИЦА","Големо летно овошје со црвена сочна внатрешност.","ОВОШЈЕ","https://images.unsplash.com/photo-1563114773-84221bd62daa?w=900"),
  Puzzle("АНАНАС","Тропско овошје со тврда кора и зелени листови.","ОВОШЈЕ","https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=900"),
  Puzzle("МОРКОВ","Портокалов коренест зеленчук што го јадеме свеж или варен.","ХРАНА","https://images.unsplash.com/photo-1445282768818-728615cc910a?w=900"),
  Puzzle("КРУША","Сочно овошје што често има форма на ѕвонче.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pear?lock=1100"),
  Puzzle("ЦРЕША","Мало црвено овошје со тврда семка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/cherry?lock=1101"),
  Puzzle("ПРАСКА","Меко овошје со кадифена кора и голема семка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/peach?lock=1102"),
  Puzzle("ГРОЗЈЕ","Мали плодови што растат во гроздови.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/grapes?lock=1103"),
  Puzzle("КИВИ","Овошје со кафеава кора и зелена внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/kiwi%20fruit?lock=1104"),
  Puzzle("КАЈСИЈА","Портокалово овошје со мека кора и една семка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/apricot?lock=1105"),
  Puzzle("ДОМАТ","Црвен плод што често се користи во салати.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/tomato?lock=1106"),
  Puzzle("КРАСТАВИЦА","Зелен долг зеленчук што често се јаде свеж.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/cucumber?lock=1107"),
  Puzzle("КОМПИР","Скробест зеленчук што се вари или пече.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/potato?lock=1108"),
  Puzzle("ОРИЗ","Мали зрна што се готват како прилог или јадење.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/rice?lock=1109"),
  Puzzle("МАНДАРИНА","Мало цитрусно овошје што лесно се лупи.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/mandarin%20orange?lock=23426"),
  Puzzle("ЛОЗОВ ЛИСТ","Лист што се користи за виткање сарми.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/grape%20leaf?lock=23494"),
  Puzzle("СМОКВА","Слатко овошје со мека внатрешност и многу ситни семки.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/fig%20fruit?lock=20295"),
  Puzzle("КАЛИНКА","Овошје со црвени зрнца внатре.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pomegranate?lock=21332"),
  Puzzle("ДИЊА","Големо слатко летно овошје со миризлива внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/melon?lock=18166"),
  Puzzle("ПИПЕРКА","Зеленчук што може да биде црвен, жолт или зелен.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/bell%20pepper?lock=21349"),
  Puzzle("КРОМИД","Зеленчук што често се сече за готвење и предизвикува солзи.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/onion?lock=20304"),
  Puzzle("ЛУК","Зачинест зеленчук со силен мирис, составен од чешниња.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/garlic?lock=17160"),
  Puzzle("СИРЕЊЕ","Млечен производ што се прави од млеко.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/cheese?lock=20285"),
  Puzzle("ЛЕБ","Основна храна што се пече од брашно и вода.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/bread?lock=17137"),
  Puzzle("МАЛИНА","Мало црвено или розово шумско овошје.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/raspberry?lock=40284"),
  Puzzle("БОРОВИНКА","Мало темносино шумско овошје.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/blueberry?lock=43438"),
  Puzzle("КАПИНА","Темно овошје што расте на бодликава грмушка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/blackberry?lock=40286"),
  Puzzle("ПАПАЈА","Тропско овошје со портокалова внатрешност и црни семки.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/papaya?lock=40262"),
  Puzzle("МАНГО","Тропско овошје со жолто-портокалова сочна внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/mango%20fruit?lock=39242"),
  Puzzle("АВОКАДО","Зелено овошје со голема семка и кремаста внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/avocado?lock=41324"),
  Puzzle("ПЕЧУРКА","Јадлива габа што се користи во многу јадења.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/mushroom?lock=41368"),
  Puzzle("ПЧЕНКА","Жолти зрна што растат на кочан.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/corn%20cob?lock=40306"),
  Puzzle("ТЕСТЕНИНИ","Храна направена од тесто, често во форма на шпагети.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pasta?lock=43465"),
  Puzzle("ЈОГУРТ","Млечен производ со киселкаст вкус.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/yogurt?lock=40302")
 )),
 GameCategory("ПРИРОДА","🌳", listOf(
  Puzzle("ПЛАНИНА","Висока природна форма на земјиште.","ПРИРОДА","https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=900"),
  Puzzle("СОНЦЕ","Ѕвезда што ја осветлува Земјата.","ПРИРОДА","https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=900"),
  Puzzle("РЕКА","Природна вода што тече низ корито.","ПРИРОДА","https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=900"),
  Puzzle("ВОДОПАД","Вода што паѓа од висока карпа или праг.","ПРИРОДА","https://images.unsplash.com/photo-1432405972618-c60b0225b8f9?w=900"),
  Puzzle("ЕЗЕРО","Голема површина со вода опкружена со копно.","ПРИРОДА","https://images.unsplash.com/photo-1470770841072-f978cf4d019e?w=900"),
  Puzzle("ПЕШТЕРА","Природна шуплина во карпа или планина.","ПРИРОДА","https://images.unsplash.com/photo-1500534623283-312aade485b7?w=900"),
  Puzzle("ВУЛКАН","Планина од која може да избива лава.","ПРИРОДА","https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=900"),
  Puzzle("ОСТРОВ","Копно опкружено со вода од сите страни.","ПРИРОДА","https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=900"),
  Puzzle("ШУМА","Голема површина покриена со многу дрвја.","ПРИРОДА","https://loremflickr.com/900/600/forest?lock=1200"),
  Puzzle("ЦВЕТ","Шарен дел од растение што често има мирис.","ПРИРОДА","https://loremflickr.com/900/600/flower?lock=1201"),
  Puzzle("ДРВО","Високо растение со стебло и гранки.","ПРИРОДА","https://loremflickr.com/900/600/tree?lock=1202"),
  Puzzle("ОБЛАК","Бела или сива маса од ситни капки на небото.","ПРИРОДА","https://loremflickr.com/900/600/cloud?lock=1203"),
  Puzzle("ДОЖД","Вода што паѓа од облаците.","ПРИРОДА","https://loremflickr.com/900/600/rain?lock=1204"),
  Puzzle("СНЕГ","Бели ледени кристали што паѓаат во зима.","ПРИРОДА","https://loremflickr.com/900/600/snow?lock=1205"),
  Puzzle("ВЕТЕР","Движење на воздухот.","ПРИРОДА","https://loremflickr.com/900/600/wind?lock=1206"),
  Puzzle("МОРЕ","Голема солена водена површина.","ПРИРОДА","https://loremflickr.com/900/600/sea?lock=1207"),
  Puzzle("ПЕСОК","Ситни зрнца што често ги има на плажа.","ПРИРОДА","https://loremflickr.com/900/600/sand?lock=1208"),
  Puzzle("ЅВЕЗДА","Светло небесно тело што се гледа ноќе.","ПРИРОДА","https://loremflickr.com/900/600/star?lock=1209"),
  Puzzle("ДОЛИНА","Ниско земјиште меѓу ридови или планини.","ПРИРОДА","https://loremflickr.com/900/600/valley?lock=13290"),
  Puzzle("РИД","Издигнат дел од земјиштето, понизок од планина.","ПРИРОДА","https://loremflickr.com/900/600/hill?lock=10148"),
  Puzzle("ЛЕДНИК", "Голема маса мраз што бавно се движи низ планина.","ПРИРОДА","https://loremflickr.com/900/600/glacier?lock=31291"),
  Puzzle("КАНЈОН","Длабока и тесна долина со стрмни карпести страни.","ПРИРОДА","https://loremflickr.com/900/600/canyon?lock=13282"),
  Puzzle("КАМЕН", "Цврст природен материјал што го има во карпите.","ПРИРОДА","https://loremflickr.com/900/600/stone%20rock?lock=30240"),
  Puzzle("МОЛЊА","Силен електричен блесок за време на бура.","ПРИРОДА","https://loremflickr.com/900/600/lightning?lock=12231"),
  Puzzle("ВИНОЖИТО","Шарен лак на небото што се појавува по дожд.","ПРИРОДА","https://loremflickr.com/900/600/rainbow?lock=15403"),
  Puzzle("МАГЛА","Ситни капки вода во воздухот што ја намалуваат видливоста.","ПРИРОДА","https://loremflickr.com/900/600/fog?lock=12226"),
  Puzzle("ИЗВОР","Место каде што водата природно излегува од земјата.","ПРИРОДА","https://loremflickr.com/900/600/spring%20water?lock=12247"),
  Puzzle("КРАЈБРЕЖЈЕ","Појасот на копно покрај море или езеро.","ПРИРОДА","https://loremflickr.com/900/600/coastline?lock=17443"),
  Puzzle("ПУСТИНА","Сува област со многу малку дожд и малку растенија.","ПРИРОДА","https://loremflickr.com/900/600/desert%20landscape?lock=34370"),
  Puzzle("МОЧУРИШТЕ","Влажно земјиште со плитка вода и водни растенија.","ПРИРОДА","https://loremflickr.com/900/600/wetland%20marsh?lock=36499"),
  Puzzle("ВОДЕНА ЛИЛЈАНА","Водно растение со големи листови што пловат на површината.","ПРИРОДА","https://loremflickr.com/900/600/water%20lily?lock=40625"),
  Puzzle("КОРАЛЕН ГРЕБЕН","Подводна градба создадена од корали.","ПРИРОДА","https://loremflickr.com/900/600/coral%20reef?lock=40664"),
  Puzzle("ГРОМ","Силен звук што се слуша за време на бура.","ПРИРОДА","https://loremflickr.com/900/600/thunderstorm?lock=31205"),
  Puzzle("МРАЗ","Тенок слој замрзната вода на студена површина.","ПРИРОДА","https://loremflickr.com/900/600/frost?lock=31195"),
  Puzzle("СЕНКА","Темна површина создадена кога нешто ја блокира светлината.","ПРИРОДА","https://loremflickr.com/900/600/tree%20shadow?lock=32245"),
  Puzzle("ПРИЛИВ","Покачување на нивото на морската вода.","ПРИРОДА","https://loremflickr.com/900/600/ocean%20tide?lock=33300"),
  Puzzle("ОСЕКА", "Период кога нивото на морската вода се намалува.","ПРИРОДА","https://loremflickr.com/900/600/low%20tide%20shore?lock=30246"),
  Puzzle("СЕВЕРНА СВЕТЛИНА","Шарени светлосни појави на небото во поларните области.","ПРИРОДА","https://loremflickr.com/900/600/aurora%20borealis?lock=42764")
 )),
 GameCategory("ПРЕВОЗ","🚗", listOf(
  Puzzle("АВТОМОБИЛ","Возило со четири тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=900"),
  Puzzle("ВЕЛОСИПЕД","Превозно средство со две тркала и педали.","ПРЕВОЗ","https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=900"),
  Puzzle("ВОЗ","Превозно средство што се движи по шини.","ПРЕВОЗ","https://loremflickr.com/900/600/railway%20station?lock=77123"),
  Puzzle("МОТОЦИКЛ","Моторно возило на две тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=900"),
  Puzzle("ТРАМВАЈ","Градско превозно средство што се движи по шини.","ПРЕВОЗ","https://images.unsplash.com/photo-1519501025264-65ba15a82390?w=900"),
  Puzzle("ХЕЛИКОПТЕР","Летало со ротори што може да полетува вертикално.","ПРЕВОЗ","https://images.unsplash.com/photo-1474302770737-173ee21bab63?w=900"),
  Puzzle("ТРОТИНЕТ","Мало превозно средство на две тркала со платформа.","ПРЕВОЗ","https://images.unsplash.com/photo-1604868189265-219b5a8a1b6a?w=900"),
  Puzzle("КАМИОН","Големо моторно возило за превоз на товар.","ПРЕВОЗ","https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=900"),
  Puzzle("АВИОН","Летало со крила што превезува патници.","ПРЕВОЗ","https://loremflickr.com/900/600/airplane?lock=1300"),
  Puzzle("БРОД","Пловило што се движи по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/ship?lock=1301"),
  Puzzle("АВТОБУС","Големо патничко возило за јавен превоз.","ПРЕВОЗ","https://loremflickr.com/900/600/bus?lock=1302"),
  Puzzle("ТАКСИ","Автомобил што превезува патници за надомест.","ПРЕВОЗ","https://loremflickr.com/900/600/taxi?lock=1303"),
  Puzzle("МЕТРО","Подземен градски железнички превоз.","ПРЕВОЗ","https://loremflickr.com/900/600/subway%20train?lock=1304"),
  Puzzle("ЧАМЕЦ","Мало пловило за движење по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/small%20boat?lock=1305"),
  Puzzle("ЕДРИЛИЦА","Пловило што се движи со помош на ветер.","ПРЕВОЗ","https://loremflickr.com/900/600/sailboat?lock=1306"),
  Puzzle("СКУТЕР","Мало моторно возило на две тркала.","ПРЕВОЗ","https://loremflickr.com/900/600/scooter?lock=1307"),
  Puzzle("ПРИКОЛКА","Возило без мотор што го влече друго возило.","ПРЕВОЗ","https://loremflickr.com/900/600/trailer?lock=1308"),
  Puzzle("БАГЕР","Работна машина што копа земја.","ПРЕВОЗ","https://loremflickr.com/900/600/excavator?lock=1309"),
  Puzzle("ТРОЛЕЈБУС","Градско електрично возило што се напојува преку жици.","ПРЕВОЗ","https://loremflickr.com/900/600/trolleybus?lock=15453"),
  Puzzle("ТРКАЛО","Кружен дел што му помага на возилото да се движи.","ПРЕВОЗ","https://loremflickr.com/900/600/wheel?lock=12309"),
  Puzzle("ПОДМОРНИЦА","Пловило што може да се движи под површината на водата.","ПРЕВОЗ","https://loremflickr.com/900/600/submarine?lock=16518"),
  Puzzle("ЖИЧНИЦА","Превозно средство што се движи високо над земјата по кабел.","ПРЕВОЗ","https://loremflickr.com/900/600/cable%20car?lock=13360"),
  Puzzle("САНКА","Превозно средство што се лизга по снег.","ПРЕВОЗ","https://loremflickr.com/900/600/sled?lock=11240"),
  Puzzle("КАРАВАН","Приколка или возило опремено за патување и престој.","ПРЕВОЗ","https://loremflickr.com/900/600/caravan?lock=13321"),
  Puzzle("КОЧИЈА","Старо превозно средство што го влечат коњи.","ПРЕВОЗ","https://loremflickr.com/900/600/horse%20carriage?lock=12287"),
  Puzzle("БУЛДОЖЕР","Тешка машина што турка земја и урнатини.","ПРЕВОЗ","https://loremflickr.com/900/600/bulldozer?lock=14396"),
  Puzzle("КОМБЕ","Помало моторно возило за превоз на луѓе или товар.","ПРЕВОЗ","https://loremflickr.com/900/600/van?lock=11242"),
  Puzzle("ТРАКТОР","Работно возило што се користи најчесто во земјоделство.","ПРЕВОЗ","https://loremflickr.com/900/600/tractor?lock=13372"),
  Puzzle("ЛОКОМОТИВА", "Дел од возот што ги влече или турка вагоните.","ПРЕВОЗ","https://loremflickr.com/900/600/train%20locomotive?lock=35503"),
  Puzzle("ТРИЦИКЛ", "Возило со три тркала.","ПРЕВОЗ","https://loremflickr.com/900/600/three%20wheeler%20vehicle?lock=32373"),
  Puzzle("САМОЛЕТ","Летало со мотори и крила.","ПРЕВОЗ","https://loremflickr.com/900/600/passenger%20airplane?lock=33357"),
  Puzzle("ГЛИСЕР","Брз моторен чамец што се движи по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/speedboat?lock=32300"),
  Puzzle("ФЕРИБРОД","Голем брод што превезува патници и автомобили.","ПРЕВОЗ","https://loremflickr.com/900/600/ferry%20boat?lock=34404"),
  Puzzle("ДИРИЖАБЛ","Летало полесно од воздухот со голем наполнет балон.","ПРЕВОЗ","https://loremflickr.com/900/600/airship%20blimp?lock=34374"),
  Puzzle("ПАРЕН ВОЗ","Стар тип воз што се движел со помош на пареа.","ПРЕВОЗ","https://loremflickr.com/900/600/steam%20locomotive?lock=34424"),
  Puzzle("РОЛЕРИ","Обувки со тркалца што се користат за возење.","ПРЕВОЗ","https://loremflickr.com/900/600/roller%20skates?lock=32310"),
  Puzzle("СКЕЈТБОРД","Даска со четири тркалца на која се вози стоејќи.","ПРЕВОЗ","https://loremflickr.com/900/600/skateboard?lock=35437"),
  Puzzle("СНЕГОМОБИЛ","Моторно возило наменето за движење по снег.","ПРЕВОЗ","https://loremflickr.com/900/600/snowmobile?lock=36498")
 )),
 GameCategory("СПОРТ","⚽", listOf(
  Puzzle("ФУДБАЛ","Спорт што се игра со топка и нозе.","СПОРТ","https://images.unsplash.com/photo-1522778119026-d647f0596c20?w=900"),
  Puzzle("КОШАРКА","Спорт во кој топката се фрла во кош.","СПОРТ","https://images.unsplash.com/photo-1519861531473-9200262188bf?w=900"),
  Puzzle("ПЛИВАЊЕ","Спортска активност што се изведува во вода.","СПОРТ","https://images.unsplash.com/photo-1530549387789-4c1017266635?w=900"),
  Puzzle("ОДБОЈКА","Спорт во кој две екипи ја префрлаат топката преку мрежа.","СПОРТ","https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?w=900"),
  Puzzle("СКИЈАЊЕ","Зимски спорт со лизгање по снег со скии.","СПОРТ","https://images.unsplash.com/photo-1517299321609-52687d1bc55a?w=900"),
  Puzzle("ВЕЛОСИПЕДИЗАМ","Спортско возење велосипед.","СПОРТ","https://images.unsplash.com/photo-1541625602330-2277e4c46182?w=900"),
  Puzzle("АТЛЕТИКА","Спорт со трчање, скокање и фрлање.","СПОРТ","https://images.unsplash.com/photo-1533561052604-c3beb6d55b8d?w=900"),
  Puzzle("ГИМНАСТИКА","Спорт со вежби за сила, рамнотежа и флексибилност.","СПОРТ","https://images.unsplash.com/photo-1518611012118-696072aa579a?w=900"),
  Puzzle("ТЕНИС","Спорт со рекет и топче преку мрежа.","СПОРТ","https://loremflickr.com/900/600/tennis?lock=1400"),
  Puzzle("БОКС","Боречки спорт во кој се удираат ракавици.","СПОРТ","https://loremflickr.com/900/600/boxing?lock=1401"),
  Puzzle("РАКОМЕТ","Тимски спорт во кој топката се фрла со рака.","СПОРТ","https://loremflickr.com/900/600/handball?lock=1402"),
  Puzzle("БЕЈЗБОЛ","Спорт со палка, топка и бази.","СПОРТ","https://loremflickr.com/900/600/baseball?lock=1403"),
  Puzzle("ГОЛФ","Спорт во кој топче се удира во дупка со палка.","СПОРТ","https://loremflickr.com/900/600/golf?lock=1404"),
  Puzzle("КАРАТЕ","Боречка вештина со удари и блокови.","СПОРТ","https://loremflickr.com/900/600/karate?lock=1405"),
  Puzzle("ЏУДО","Боречки спорт со фрлања и зафати.","СПОРТ","https://loremflickr.com/900/600/judo?lock=1406"),
  Puzzle("ЈАВАЊЕ","Спортска активност на коњ.","СПОРТ","https://loremflickr.com/900/600/horse%20riding?lock=1407"),
  Puzzle("КАЈАК","Веслачки спорт со тесен чамец.","СПОРТ","https://loremflickr.com/900/600/kayak?lock=1408"),
  Puzzle("КЛИЗГАЊЕ","Движење по мраз со лизгалки.","СПОРТ","https://loremflickr.com/900/600/ice%20skating?lock=1409"),
  Puzzle("ПИНГ-ПОНГ","Игра со мали рекети и топче на маса.","СПОРТ","https://loremflickr.com/900/600/table%20tennis?lock=13449"),
  Puzzle("КУГЛАЊЕ","Спорт во кој се соборуваат кегли со тешка топка.","СПОРТ","https://loremflickr.com/900/600/bowling?lock=12322"),
  Puzzle("СТРЕЛАШТВО","Спорт во кој се погодува цел со лак или оружје.","СПОРТ","https://loremflickr.com/900/600/archery%20target%20sport?lock=15525"),
  Puzzle("ФЕХТУВАЊЕ","Боречки спорт со мечеви и заштитна опрема.","СПОРТ","https://loremflickr.com/900/600/fencing?lock=14444"),
  Puzzle("РЕГБИ","Тимски спорт со овална топка.","СПОРТ","https://loremflickr.com/900/600/rugby?lock=10233"),
  Puzzle("КРИКЕТ","Тимски спорт со палка и топка, популарен во многу земји.","СПОРТ","https://loremflickr.com/900/600/cricket?lock=11307"),
  Puzzle("СНОУБОРД","Зимски спорт со една широка даска на снег.","СПОРТ","https://loremflickr.com/900/600/snowboard?lock=13418"),
  Puzzle("СУРФАЊЕ","Спорт во кој се вози на бранови со даска.","СПОРТ","https://loremflickr.com/900/600/surfing?lock=12351"),
  Puzzle("МАРАТОН","Долга трка во трчање на 42 километри.","СПОРТ","https://loremflickr.com/900/600/marathon%20running?lock=12353"),
  Puzzle("КАРЛИНГ","Зимски тимски спорт во кој камења се лизгаат по мраз.","СПОРТ","https://loremflickr.com/900/600/curling?lock=12341"),
  Puzzle("ФУТСАЛ","Варијанта на фудбал што се игра во сала со помал тим.","СПОРТ","https://loremflickr.com/900/600/futsal?lock=31325"),
  Puzzle("ВЕСЛАЊЕ","Спорт во кој спортистите придвижуваат чамец со весла.","СПОРТ","https://loremflickr.com/900/600/rowing%20sport?lock=32314"),
  Puzzle("ТРИАТЛОН","Натпревар што комбинира пливање, велосипедизам и трчање.","СПОРТ","https://loremflickr.com/900/600/triathlon?lock=33418"),
  Puzzle("ПАРКУР","Спортска активност со прескокнување пречки во урбана средина.","СПОРТ","https://loremflickr.com/900/600/parkour?lock=31316"),
  Puzzle("КАЧУВАЊЕ","Спорт во кој се искачуваат карпи или вештачки ѕидови.","СПОРТ","https://loremflickr.com/900/600/rock%20climbing?lock=33373"),
  Puzzle("ДИГАЊЕ ТЕГОВИ","Спорт во кој натпреварувачите креваат тешки тегови.","СПОРТ","https://loremflickr.com/900/600/weightlifting?lock=37576"),
  Puzzle("БОРЕЊЕ","Боречки спорт во кој двајца натпреварувачи се обидуваат да надвладеат.","СПОРТ","https://loremflickr.com/900/600/wrestling?lock=31275"),
  Puzzle("БАДМИНТОН","Спорт со рекети во кој се удира пердувесто топче.","СПОРТ","https://loremflickr.com/900/600/badminton?lock=34443"),
  Puzzle("СЕПАКТАКРАВ","Тимски спорт од Југоисточна Азија во кој топката се удира со нозе.","СПОРТ","https://loremflickr.com/900/600/sepak%20takraw?lock=36533"),
  Puzzle("СОФТБОЛ","Тимски спорт сличен на бејзбол, со поголема топка.","СПОРТ","https://loremflickr.com/900/600/softball?lock=32375")
 )),
 GameCategory("МЕСТА","🏙️", listOf(
  Puzzle("УЧИЛИШТЕ","Место каде што децата учат.","МЕСТА","https://images.unsplash.com/photo-1580582932707-520aed937b7b?w=900"),
  Puzzle("БИБЛИОТЕКА","Место каде што се позајмуваат книги.","МЕСТА","https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=900"),
  Puzzle("БОЛНИЦА","Место каде што лекарите лекуваат болни луѓе.","МЕСТА","https://images.unsplash.com/photo-1586773860418-d37222d8fce3?w=900"),
  Puzzle("ТЕАТАР","Зграда каде што се изведуваат претстави.","МЕСТА","https://images.unsplash.com/photo-1503095396549-807759245b35?w=900"),
  Puzzle("СТАДИОН","Голем простор каде што се одржуваат спортски натпревари.","МЕСТА","https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=900"),
  Puzzle("АЕРОДРОМ","Место каде што полетуваат и слетуваат авиони.","МЕСТА","https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=900"),
  Puzzle("ЖЕЛЕЗНИЧКА СТАНИЦА","Место каде што запираат и тргнуваат возови.","МЕСТА","https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=900"),
  Puzzle("ПАЗАР","Место каде што луѓето купуваат свежа храна и други производи.","МЕСТА","https://images.unsplash.com/photo-1488459716781-31db52582fe9?w=900"),
  Puzzle("МУЗЕЈ","Место каде што се изложуваат историски и уметнички предмети.","МЕСТА","https://loremflickr.com/900/600/museum?lock=1500"),
  Puzzle("ПЛАЖА","Место покрај море или езеро со песок или камчиња.","МЕСТА","https://loremflickr.com/900/600/beach?lock=1501"),
  Puzzle("ЦРКВА","Верски објект каде што се одржуваат христијански богослужби.","МЕСТА","https://loremflickr.com/900/600/church?lock=1502"),
  Puzzle("ЏАМИЈА","Верски објект каде што муслиманите се молат.","МЕСТА","https://loremflickr.com/900/600/mosque?lock=1503"),
  Puzzle("РЕСТОРАН","Место каде што се подготвува и служи храна.","МЕСТА","https://loremflickr.com/900/600/restaurant?lock=1504"),
  Puzzle("ХОТЕЛ","Место каде што патниците престојуваат и спијат.","МЕСТА","https://loremflickr.com/900/600/hotel?lock=1505"),
  Puzzle("БАНКА","Место каде што се чуваат и управуваат пари.","МЕСТА","https://loremflickr.com/900/600/bank%20building?lock=1506"),
  Puzzle("ПОШТА","Место каде што се испраќаат писма и пакети.","МЕСТА","https://loremflickr.com/900/600/post%20office?lock=1507"),
  Puzzle("ПАРК","Јавно зелено место за прошетка и одмор.","МЕСТА","https://loremflickr.com/900/600/city%20park?lock=1508"),
  Puzzle("ФАРМА","Место каде што се одгледуваат животни или растенија.","МЕСТА","https://loremflickr.com/900/600/farm?lock=1509"),
  Puzzle("БЕНЗИНСКА ПУМПА","Место каде што возилата полнат гориво.","МЕСТА","https://loremflickr.com/900/600/gas%20station?lock=19727"),
  Puzzle("АПТЕКА","Место каде што се купуваат лекови и медицински производи.","МЕСТА","https://loremflickr.com/900/600/pharmacy?lock=11288"),
  Puzzle("ПЕКАРНИЦА","Продавница каде што се продава свеж леб и печива.","МЕСТА","https://loremflickr.com/900/600/bakery?lock=14449"),
  Puzzle("СУПЕРМАРКЕТ","Голема продавница за храна и секојдневни производи.","МЕСТА","https://loremflickr.com/900/600/supermarket?lock=16573"),
  Puzzle("ПОЖАРНА СТАНИЦА","Место од каде што пожарникарите излегуваат на интервенции.","МЕСТА","https://loremflickr.com/900/600/fire%20station?lock=19734"),
  Puzzle("ПОЛИЦИСКА СТАНИЦА","Место каде што работи полицијата.","МЕСТА","https://loremflickr.com/900/600/police%20station?lock=21855"),
  Puzzle("ГРАДИНКА","Место каде што престојуваат и учат мали деца.","МЕСТА","https://loremflickr.com/900/600/kindergarten?lock=13374"),
  Puzzle("АВТОБУСКА СТАНИЦА","Место каде што пристигнуваат и поаѓаат автобуси.","МЕСТА","https://loremflickr.com/900/600/bus%20station?lock=21831"),
  Puzzle("ЗООЛОШКА ГРАДИНА","Место каде што посетителите гледаат различни животни.","МЕСТА","https://loremflickr.com/900/600/zoo?lock=20770"),
  Puzzle("БАЗЕН","Место исполнето со вода за пливање.","МЕСТА","https://loremflickr.com/900/600/swimming%20pool?lock=10226"),
  Puzzle("ОПШТИНА","Јавна установа што ги организира локалните работи во една општина.","МЕСТА","https://loremflickr.com/900/600/municipal%20building?lock=32372"),
  Puzzle("СУД","Институција каде што судии решаваат правни спорови.","МЕСТА","https://loremflickr.com/900/600/courthouse?lock=28160"),
  Puzzle("ЗАТВОР","Објект во кој се сместуваат лица осудени на затворска казна.","МЕСТА","https://loremflickr.com/900/600/prison%20building?lock=31297"),
  Puzzle("УНИВЕРЗИТЕТ","Високообразовна установа каде што се студира.","МЕСТА","https://loremflickr.com/900/600/university%20campus?lock=36559"),
  Puzzle("ЛАБОРАТОРИЈА","Просторија каде што се вршат научни испитувања и експерименти.","МЕСТА","https://loremflickr.com/900/600/science%20laboratory?lock=37570"),
  Puzzle("ОПЕРСКА КУЌА","Зграда каде што се изведуваат оперски претстави.","МЕСТА","https://loremflickr.com/900/600/opera%20house?lock=36574"),
  Puzzle("КИНО","Место каде што се прикажуваат филмови на голем екран.","МЕСТА","https://loremflickr.com/900/600/movie%20theater?lock=29205"),
  Puzzle("ЗАБАВЕН ПАРК","Место со атракции, вртелешки и тобогани.","МЕСТА","https://loremflickr.com/900/600/amusement%20park?lock=36541"),
  Puzzle("ПРИСТАНИШТЕ","Место покрај вода каде што бродовите пристануваат.","МЕСТА","https://loremflickr.com/900/600/harbor%20port?lock=36582"),
  Puzzle("СВЕТИЛНИК","Висока кула покрај море што им помага на бродовите да се ориентираат.","МЕСТА","https://loremflickr.com/900/600/lighthouse?lock=34452")
 ))
)
fun normalizeAnswer(value:String):String {
 var s=value.trim().lowercase(Locale.ROOT).replace("dzh","џ").replace("dž","џ").replace("gj","ѓ").replace("zh","ж").replace("dz","ѕ").replace("lj","љ").replace("nj","њ").replace("kj","ќ").replace("ch","ч").replace("sh","ш")
 val latin=mapOf('a' to 'а','b' to 'б','v' to 'в','g' to 'г','d' to 'д','e' to 'е','z' to 'з','i' to 'и','j' to 'ј','k' to 'к','l' to 'л','m' to 'м','n' to 'н','o' to 'о','p' to 'п','r' to 'р','s' to 'с','t' to 'т','u' to 'у','f' to 'ф','h' to 'х','c' to 'ц')
 s=s.map { latin[it] ?: it }.joinToString("")
 return s.uppercase(Locale.ROOT).filter { it.isLetter() }
}
class MainActivity:ComponentActivity(){
 private val prefs by lazy { getSharedPreferences("app_updater", Context.MODE_PRIVATE) }
 private val downloadReceiver = object: BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
   if(intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
   val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
   if(id != prefs.getLong("download_id", -2L)) return
   val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
   val uri = manager.getUriForDownloadedFile(id)
   if(uri != null) { prefs.edit().putString("apk_uri", uri.toString()).apply(); installDownloadedApk() }
   else Toast.makeText(this@MainActivity, "Le téléchargement a échoué. Réessaie.", Toast.LENGTH_LONG).show()
  }
 }
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  val filter=IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
  if(Build.VERSION.SDK_INT >= 33) registerReceiver(downloadReceiver,filter,Context.RECEIVER_EXPORTED) else registerReceiver(downloadReceiver,filter)
  setContent { GameScreen(onExit={finishAndRemoveTask()}, onUpdate={url -> downloadUpdate(url)}) }
 }
 override fun onResume(){
  super.onResume()
  val pending=prefs.getString("apk_uri",null)
  if(pending!=null && (Build.VERSION.SDK_INT < 26 || packageManager.canRequestPackageInstalls())) installDownloadedApk()
 }
 private fun downloadUpdate(url:String) {
  val request=DownloadManager.Request(Uri.parse(url))
   .setTitle("Pogodi Gi Zborovite")
   .setDescription("Téléchargement de la mise à jour…")
   .setMimeType("application/vnd.android.package-archive")
   .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
   .setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,"Pogodi-Gi-Zborovite-update.apk")
  val id=(getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
  prefs.edit().putLong("download_id",id).remove("apk_uri").apply()
  Toast.makeText(this,"Téléchargement démarré. Tu recevras une notification.",Toast.LENGTH_LONG).show()
 }
 private fun installDownloadedApk() {
  val raw=prefs.getString("apk_uri",null) ?: return
  if(Build.VERSION.SDK_INT >= 26 && !packageManager.canRequestPackageInstalls()) {
   startActivity(Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,Uri.parse("package:$packageName")))
   return
  }
  val uri=Uri.parse(raw)
  prefs.edit().remove("apk_uri").apply()
  val install=Intent(Intent.ACTION_VIEW).setDataAndType(uri,"application/vnd.android.package-archive")
   .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
  try { startActivity(install) } catch(e:Exception) { Toast.makeText(this,"Impossible d’ouvrir l’installateur Android.",Toast.LENGTH_LONG).show() }
 }
 override fun onDestroy(){unregisterReceiver(downloadReceiver);super.onDestroy()}
}
class UpdateRestartReceiver:BroadcastReceiver() {
 override fun onReceive(context:Context,intent:Intent) {
  if(intent.action==Intent.ACTION_MY_PACKAGE_REPLACED) {
   val launch=context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return
   launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
   try { context.startActivity(launch) } catch (_:Exception) {}
  }
 }
}
fun playGameSound(context:Context,tone:Int,enabled:Boolean,volume:Float=0.75f) {
 if(enabled && volume>0f) {
  try {
   val level=(volume.coerceIn(0f,1f)*100).toInt().coerceIn(1,100)
   val g=ToneGenerator(AudioManager.STREAM_MUSIC,level)
   val duration=if(tone==ToneGenerator.TONE_PROP_ACK) 300 else if(tone==ToneGenerator.TONE_PROP_NACK) 360 else 110
   g.startTone(tone,duration)
   android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({g.release()},duration.toLong()+80)
  } catch (_:Exception) {}
 }
}
private class BackgroundMusicPlayer(private val volume:Float) {
 @Volatile private var running = false
 private var track: AudioTrack? = null
 private var worker: Thread? = null
 fun start() {
  if (running) return
  val rate = 22050
  val format = AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(rate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build()
  val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
  val size = maxOf(AudioTrack.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT), 4096)
  val audio = AudioTrack.Builder().setAudioAttributes(attributes).setAudioFormat(format).setBufferSizeInBytes(size).setTransferMode(AudioTrack.MODE_STREAM).build()
  audio.setVolume(volume.coerceIn(0f,1f))
  audio.play()
  track = audio
  running = true
  worker = Thread {
   val buffer = ShortArray(2048)
   val melody = doubleArrayOf(261.63, 329.63, 392.00, 329.63, 293.66, 349.23, 440.00, 349.23)
   var sample = 0L
   try {
    while (running) {
     for (i in buffer.indices) {
      val beatSamples = (rate * 1.35).toLong()
      val beat = (sample / beatSamples).toInt() % melody.size
      val within = (sample % beatSamples).toDouble() / beatSamples
      val fade = kotlin.math.sin(Math.PI * within).coerceAtLeast(0.0)
      val frequency = melody[beat]
      val time = sample.toDouble() / rate
      val softTone = kotlin.math.sin(2.0 * Math.PI * frequency * time) * 0.62 +
       kotlin.math.sin(2.0 * Math.PI * (frequency / 2.0) * time) * 0.20 +
       kotlin.math.sin(2.0 * Math.PI * (frequency * 1.5) * time) * 0.08
      val value = softTone * (0.45 + 0.25 * fade) * 2600.0
      buffer[i] = value.toInt().coerceIn(-32768, 32767).toShort()
      sample++
     }
     audio.write(buffer, 0, buffer.size, AudioTrack.WRITE_BLOCKING)
    }
   } catch (_: Exception) { }
  }.apply { name = "PogodiBackgroundMusic"; isDaemon = true; start() }
 }
 fun stop() {
  running = false
  try { track?.pause(); track?.flush(); track?.stop() } catch (_: Exception) { }
  try { track?.release() } catch (_: Exception) { }
  track = null
  worker = null
 }
}


private fun radioUiText(lang:String,key:String):String {
 val values=mapOf(
  "mk" to mapOf("country" to "РАДИО ЗЕМЈА","preparing" to "Се подготвува…"),
  "fr" to mapOf("country" to "PAYS DE LA RADIO","preparing" to "Préparation…"),
  "sq" to mapOf("country" to "SHTETI I RADIOS","preparing" to "Duke u përgatitur…"),
  "en" to mapOf("country" to "RADIO COUNTRY","preparing" to "Preparing…")
 )
 return values[lang]?.get(key) ?: values["mk"]!!.getValue(key)
}
private fun radioCountryName(code:String,lang:String):String {
 if(code.isBlank()) return mapOf("mk" to "Сите земји","fr" to "Tous les pays","sq" to "Të gjitha shtetet","en" to "All countries")[lang] ?: "Сите земји"
 val names=mapOf(
  "MK" to mapOf("mk" to "Македонија","fr" to "Macédoine","sq" to "Maqedonia","en" to "North Macedonia"),
  "FR" to mapOf("mk" to "Франција","fr" to "France","sq" to "Franca","en" to "France"),
  "AL" to mapOf("mk" to "Албанија","fr" to "Albanie","sq" to "Shqipëria","en" to "Albania"),
  "XK" to mapOf("mk" to "Косово","fr" to "Kosovo","sq" to "Kosova","en" to "Kosovo")
 )
 return names[code]?.get(lang) ?: Locale("",code).getDisplayCountry(Locale.forLanguageTag(lang)).ifBlank { code }
}
private fun translateRadioStatus(status:String,lang:String):String {
 val translations=mapOf(
  "Се бараат радио станици…" to listOf("Се бараат радио станици…","Recherche de stations radio…","Po kërkohen stacione radio…","Searching for radio stations…"),
  "Нема радио станици" to listOf("Нема радио станици","Aucune station radio","Nuk ka stacione radio","No radio stations"),
  "Пробај друга земја" to listOf("Пробај друга земја","Essaie un autre pays","Provo një shtet tjetër","Try another country"),
  "Паузирано" to listOf("Паузирано","En pause","Në pauzë","Paused"),
  "Се репродуцира" to listOf("Се репродуцира","Lecture en cours","Po luhet","Playing"),
  "Се поврзува…" to listOf("Се поврзува…","Connexion…","Duke u lidhur…","Connecting…"),
  "Станицата не работи – се бара друга…" to listOf("Станицата не работи – се бара друга…","La station ne fonctionne pas – recherche d’une autre…","Stacioni nuk punon – po kërkohet një tjetër…","Station unavailable – finding another…"),
  "Не може да се пушти – следна станица" to listOf("Не може да се пушти – следна станица","Impossible de lire – station suivante","Nuk mund të luhet – stacioni tjetër","Can't play – next station")
 )
 val index=when(lang){"fr"->1;"sq"->2;"en"->3;else->0}
 return translations[status]?.get(index) ?: status
}

private data class RadioStation(val name:String,val url:String)
private data class RadioCountry(val name:String,val code:String)
private val radioCountries = listOf(
  RadioCountry("Македонија","MK"),
  RadioCountry("Франција","FR"),
  RadioCountry("Албанија","AL"),
  RadioCountry("Косово","XK")
 )

class OnlineHipHopRadio(context: Context, private val onState:(String,String,Boolean)->Unit) {
 private val appContext = context.applicationContext
 private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
 private var requestJob: Job? = null
 private var player: MediaPlayer? = null
 private var stations: List<RadioStation> = emptyList()
 private var currentIndex = -1
 private var countryCode = "MK"
 private var countryName = "Македонија"
 private var genreName = "Сите категории"
 private var genreTag = ""
 private var volume = 0.13f
 private var playing = false
 private var currentName = "Hip-Hop Radio"

 fun setVolume(value: Float) {
  volume = value.coerceIn(0f, 1f)
  try { player?.setVolume(volume, volume) } catch (_: Exception) {}
 }

 fun setCountry(name:String, code:String) {
  if (countryCode == code && countryName == name) return
  countryName = name
  countryCode = code
  resetStations()
 }

 fun setGenre(name:String, tag:String) {
  if (genreName == name && genreTag == tag) return
  genreName = name
  genreTag = tag
  resetStations()
 }

 private fun resetStations() {
  requestJob?.cancel()
  stations = emptyList()
  currentIndex = -1
  stopCurrent()
 }

 fun play() {
  volume = volume.coerceIn(0f, 1f)
  requestJob?.cancel()
  requestJob = scope.launch {
   if (stations.isEmpty()) {
    onState(countryName,"Се бараат радио станици…",false)
    stations = withContext(Dispatchers.IO) { findStations() }
   }
   if (stations.isEmpty()) {
    onState("Нема радио станици","Пробај друга земја",false)
   } else {
    currentIndex = if (currentIndex < 0) 0 else currentIndex % stations.size
    startStation()
   }
  }
 }

 fun next() {
  if (stations.isEmpty()) { play(); return }
  currentIndex = (currentIndex + 1) % stations.size
  startStation()
 }

 fun previous() {
  if (stations.isEmpty()) { play(); return }
  currentIndex = if (currentIndex <= 0) stations.lastIndex else currentIndex - 1
  startStation()
 }

 fun togglePlayback() {
  try {
   val p = player
   if (p == null) { play(); return }
   if (p.isPlaying) {
    p.pause(); playing = false
    onState(currentName,"Паузирано",false)
   } else {
    p.start(); playing = true
    onState(currentName,"Се репродуцира",true)
   }
  } catch (_: Exception) { next() }
 }

  private fun findStations(): List<RadioStation> {
  val hosts = listOf("https://de1.api.radio-browser.info", "https://de2.api.radio-browser.info", "https://nl1.api.radio-browser.info")
  val found = mutableListOf<RadioStation>()
  for (host in hosts) {
   try {
    val countryFilter = if (countryCode.isBlank()) "" else "&countrycode=$countryCode"
    val preferred = if (countryCode == "FR") listOf("Skyrock","RTL","NRJ","France Inter","RMC","Europe 1","Nostalgie","Fun Radio","RTL2","Skyrock Klassiks") else emptyList()
    for (stationName in preferred) {
     try {
      val endpoint = URL("$host/json/stations/search?name=" + java.net.URLEncoder.encode(stationName,"UTF-8") + countryFilter + "&is_https=true&hidebroken=true&limit=8&order=clickcount&reverse=true")
      val connection = endpoint.openConnection()
      connection.setRequestProperty("User-Agent","PogodiGiZborovite-Android/2.3.1")
      connection.connectTimeout=8000
      connection.readTimeout=10000
      val array = JSONArray(connection.getInputStream().bufferedReader().use { it.readText() })
      for (i in 0 until array.length()) {
       val station = array.optJSONObject(i) ?: continue
       if (station.optInt("lastcheckok",0) != 1 || station.optInt("hls",0) == 1) continue
       val stream = station.optString("url_resolved").ifBlank { station.optString("url") }
       val name = station.optString("name").ifBlank { stationName }
       if (stream.startsWith("https://") && found.none { it.url == stream }) found.add(RadioStation(name,stream))
      }
     } catch (_:Exception) {}
    }
    val genreFilter = if (genreTag.isBlank()) "" else "tag=" + java.net.URLEncoder.encode(genreTag,"UTF-8") + "&"
    val endpoint = URL("$host/json/stations/search?" + (if(countryCode.isBlank()) "" else "countrycode=$countryCode&") + genreFilter + "is_https=true&hidebroken=true&limit=100&order=clickcount&reverse=true")
    val connection = endpoint.openConnection()
    connection.setRequestProperty("User-Agent","PogodiGiZborovite-Android/2.3.1")
    connection.connectTimeout=10000
    connection.readTimeout=12000
    val array = JSONArray(connection.getInputStream().bufferedReader().use { it.readText() })
    for (i in 0 until array.length()) {
     val station = array.optJSONObject(i) ?: continue
     if (station.optInt("lastcheckok",0) != 1 || station.optInt("hls",0) == 1) continue
     val stream = station.optString("url_resolved").ifBlank { station.optString("url") }
     val name = station.optString("name").ifBlank { "Radio" }
     if (stream.startsWith("https://") && found.none { it.url == stream }) found.add(RadioStation(name,stream))
    }
    if (found.isNotEmpty()) return found
   } catch (_:Exception) {}
  }
  return found
 }
private fun startStation() {
  if (currentIndex !in stations.indices) return
  stopCurrent()
  val station = stations[currentIndex]
  currentName = station.name
  onState(currentName,"Се поврзува…",false)
  try {
   val media = MediaPlayer()
   player = media
   media.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
   media.setDataSource(appContext, Uri.parse(station.url))
   media.setOnPreparedListener {
    try {
     it.setVolume(volume, volume)
     it.start(); playing = true
     onState(currentName,"Се репродуцира",true)
    } catch (_: Exception) { next() }
   }
   media.setOnErrorListener { mp, _, _ ->
    try { mp.release() } catch (_: Exception) {}
    if (player === mp) player = null
    playing = false
    onState(currentName,"Станицата не работи – се бара друга…",false)
    next()
    true
   }
   media.prepareAsync()
  } catch (_: Exception) {
   stopCurrent()
   onState(currentName,"Не може да се пушти – следна станица",false)
   next()
  }
 }

 private fun stopCurrent() {
  val old = player
  player = null
  playing = false
  try { old?.setOnPreparedListener(null); old?.setOnErrorListener(null); old?.release() } catch (_: Exception) {}
 }

 fun stop() {
  requestJob?.cancel()
  requestJob = null
  stopCurrent()
 }

 fun release() {
  stop()
  scope.coroutineContext[Job]?.cancel()
 }
}

@Composable fun GameScreen(onExit:()->Unit,onUpdate:(String)->Unit){
 val context=LocalContext.current
 val scope= rememberCoroutineScope()
 var updateStatus by remember { mutableStateOf("") }
 var showExitDialog by remember { mutableStateOf(false) }
 var showSettings by remember { mutableStateOf(false) }
 val settings=remember { context.getSharedPreferences("game_settings",Context.MODE_PRIVATE) }
 val supportedLanguages = listOf(
  "Македонски" to "mk",
  "Français" to "fr",
  "Shqip" to "sq",
  "English" to "en"
 )
 var selectedLanguage by remember { mutableStateOf((settings.getString("app_language","mk") ?: "mk").takeIf { it in setOf("mk","fr","sq","en") } ?: "mk") }
 var languageMenuExpanded by remember { mutableStateOf(false) }
 val selectedLanguageName = supportedLanguages.firstOrNull { it.second == selectedLanguage }?.first ?: "Македонски"
 var darkMode by remember { mutableStateOf(settings.getBoolean("dark_mode",true)) }
 var soundsOn by remember { mutableStateOf(settings.getBoolean("sounds_on",true)) }
 var youtubeQuery by remember { mutableStateOf("") }
 var youtubeSearchUrl by remember { mutableStateOf("") }
 var musicWebView by remember { mutableStateOf<WebView?>(null) }
 val youtubePlayerHeight = (LocalConfiguration.current.screenHeightDp.dp * 0.58f).coerceIn(340.dp, 500.dp)
 val youtubeScrollStepPx = (youtubePlayerHeight.value * LocalContext.current.resources.displayMetrics.density * 0.75f).toInt()
 val pageScrollState = rememberScrollState()
 var soundVolume by remember { mutableFloatStateOf(settings.getFloat("sound_volume",0.75f)) }
 BackHandler(enabled = youtubeSearchUrl.isNotBlank()) {
  val player = musicWebView
  if (player?.canGoBack() == true) player.goBack() else { youtubeSearchUrl = ""; musicWebView = null }
 }
 BackHandler(enabled = youtubeSearchUrl.isBlank()) { onExit() }
 fun searchYoutubeMusic() { val query=youtubeQuery.trim(); if(query.isBlank())return; youtubeSearchUrl="https://m.youtube.com/results?search_query="+java.net.URLEncoder.encode(query,"UTF-8") }
 fun play(tone:Int) { playGameSound(context,tone,soundsOn,soundVolume) }
 val pageBg=if(darkMode) Color(0xFF101B3D) else Color(0xFFF3F6FC)
 val panel=if(darkMode) Color(0xFF24365F) else Color.White
 val mainText=if(darkMode) Color.White else Color(0xFF17233D)
 val secondaryText=if(darkMode) Color(0xFFB8C8EA) else Color(0xFF52627A)
 val gold=if(darkMode) Color(0xFFFFD782) else Color(0xFF805400)
 var selected by remember { mutableIntStateOf(-1) }; var level by remember { mutableIntStateOf(0) }
 var answer by remember { mutableStateOf("") }; var score by remember { mutableIntStateOf(settings.getInt("score",0)) }; var correctAnswers by remember { mutableIntStateOf(settings.getInt("correct_answers",0)) }
 var solved by remember { mutableStateOf(false) }; var hint by remember { mutableStateOf(false) }; var wrongAttempts by remember { mutableIntStateOf(0) }
 var message by remember { mutableStateOf("Избери категорија за да започнеш!") }
 LaunchedEffect(solved) { if (solved && selected >= 0) { delay(900); val cat=categories[selected]; if (level+1 < cat.puzzles.size) { level++; message=extraText("enter_word",selectedLanguage) } else { selected=-1; message="Категоријата е завршена! Браво! 🎉" }; answer=""; solved=false; hint=false; wrongAttempts=0 } }
 MaterialTheme(colorScheme=if(darkMode) darkColorScheme(primary=Color(0xFF8DE1D2)) else lightColorScheme(primary=Color(0xFF008F83))){
 Box(Modifier.fillMaxSize().background(pageBg)) {
 Column(Modifier.fillMaxSize().verticalScroll(pageScrollState).padding(start=18.dp,end=18.dp,top=18.dp,bottom=88.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(14.dp)){
 Text("🧩 "+when(selectedLanguage){"fr"->"DEVINE LES MOTS";"sq"->"GJEJI FJALËT";"en"->"GUESS THE WORDS";else->"ПОГОДИ ГИ ЗБОРОВИТЕ"},color=mainText,fontSize=24.sp,fontWeight=FontWeight.ExtraBold,textAlign=TextAlign.Center)
 Box(Modifier.fillMaxWidth()) {
  OutlinedButton(onClick={languageMenuExpanded=true},modifier=Modifier.fillMaxWidth()) {
   Text("🌍 "+when(selectedLanguage){"fr"->"LANGUE";"sq"->"GJUHA";"en"->"LANGUAGE";else->"ЈАЗИК"}+": $selectedLanguageName  ▾",fontSize=14.sp)
  }
  DropdownMenu(expanded=languageMenuExpanded,onDismissRequest={languageMenuExpanded=false}) {
   supportedLanguages.forEach { language ->
    DropdownMenuItem(text={Text(language.first)},onClick={
     selectedLanguage=language.second
     settings.edit().putString("app_language",language.second).apply()
     languageMenuExpanded=false
    })
   }
  }
 }
 Text(when(selectedLanguage){"fr"->"Jeu de mots";"sq"->"Lojë me fjalë";"en"->"Word game";else->"Игра со зборови"},color=secondaryText)
 Card(colors=CardDefaults.cardColors(containerColor=panel),shape=RoundedCornerShape(14.dp),modifier=Modifier.fillMaxWidth()) {
  Column(Modifier.fillMaxWidth().padding(12.dp),verticalArrangement=Arrangement.spacedBy(9.dp)) {
   Text("▶ YouTube Music",color=mainText,fontWeight=FontWeight.Bold,fontSize=16.sp)
   Text(when(selectedLanguage){"fr"->"Recherche ta musique préférée";"sq"->"Kërko muzikën tënde të preferuar";"en"->"Search for your favourite music";else->"Пребарај ја омилената музика"},color=secondaryText,fontSize=11.sp)
   OutlinedTextField(value=youtubeQuery,onValueChange={youtubeQuery=it},modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text(when(selectedLanguage){"fr"->"Titre ou artiste";"sq"->"Titulli ose artisti";"en"->"Song or artist";else->"Наслов или изведувач"})},placeholder={Text("Ex. : Sami Yusuf")})
   Button(onClick={searchYoutubeMusic()},enabled=youtubeQuery.isNotBlank(),modifier=Modifier.fillMaxWidth()) { Text("🔎 "+when(selectedLanguage){"fr"->"Rechercher et écouter";"sq"->"Kërko dhe dëgjo";"en"->"Search and listen";else->"Пребарај и слушај"}) }
   if(youtubeSearchUrl.isBlank()) {
    Text(when(selectedLanguage){"fr"->"Recherche et écoute sans quitter le jeu.";"sq"->"Kërko dhe dëgjo pa dalë nga loja.";"en"->"Search and listen without leaving the game.";else->"Пребарувај и слушај без да ја напуштиш играта."},color=secondaryText,fontSize=10.sp)
   }
   if(youtubeSearchUrl.isNotBlank()) {
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween) {
     Text(when(selectedLanguage){"fr"->"Lecteur YouTube";"sq"->"YouTube luajtësi";"en"->"YouTube player";else->"YouTube плеер"},color=mainText,fontWeight=FontWeight.SemiBold,fontSize=13.sp)
     TextButton(onClick={
      val player=musicWebView
      if(player?.canGoBack()==true) player.goBack() else { youtubeSearchUrl=""; musicWebView=null }
     }) { Text(when(selectedLanguage){"fr"->"← Retour";"sq"->"← Mbrapa";"en"->"← Back";else->"← Назад"}) }
    }
    Box(Modifier.fillMaxWidth().height(youtubePlayerHeight)) {
     AndroidView(
      modifier=Modifier.fillMaxSize(),
      factory={ ctx ->
       WebView(ctx).also { player ->
        player.settings.javaScriptEnabled=true
        player.settings.domStorageEnabled=true
        player.settings.mediaPlaybackRequiresUserGesture=true
        player.settings.javaScriptCanOpenWindowsAutomatically=false
        player.settings.setSupportMultipleWindows(false)
        player.settings.useWideViewPort=false
        player.settings.loadWithOverviewMode=false
        player.settings.textZoom=100
        player.webViewClient=object:WebViewClient() {
         override fun onPageFinished(view:WebView?, url:String?) {
          super.onPageFinished(view,url)
          view?.evaluateJavascript("""
           (function() {
            var vp=document.querySelector('meta[name="viewport"]');
            if(!vp){vp=document.createElement('meta');vp.name='viewport';document.head.appendChild(vp);}
            vp.content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no';
            var style=document.getElementById('app-youtube-mobile-fix');
            if(!style){style=document.createElement('style');style.id='app-youtube-mobile-fix';document.head.appendChild(style);}
            style.textContent='html,body{width:100%!important;max-width:100%!important;min-width:0!important;margin:0!important;padding:0!important;overflow-x:hidden!important;box-sizing:border-box!important}*,*:before,*:after{box-sizing:border-box!important}ytm-pivot-bar-renderer{display:flex!important;position:fixed!important;left:0!important;right:0!important;bottom:0!important;width:100vw!important;max-width:100vw!important;margin:0!important;padding:0!important;z-index:999999!important;background:var(--yt-spec-base-background,#fff)!important;box-shadow:0 -2px 8px rgba(0,0,0,.18)!important}ytm-pivot-bar-renderer>*{max-width:100%!important}body{padding-bottom:58px!important}ytm-navigation-drawer-renderer,ytm-guide-renderer,ytm-mini-guide-renderer,ytd-mini-guide-renderer,ytd-guide-renderer,tp-yt-app-drawer,#guide,#guide-content,#secondary{display:none!important}ytm-app,ytm-browse,ytm-search,#page-manager,ytd-page-manager,#columns,#primary,#primary-inner,#content,#contents,ytm-section-list-renderer,ytm-rich-grid-renderer,ytm-item-section-renderer{width:100%!important;max-width:100%!important;min-width:0!important;margin-left:0!important;margin-right:0!important;padding-left:0!important;padding-right:0!important;left:0!important;transform:none!important}ytm-browse[has-side-navigation] #contents,ytm-browse[has-side-navigation] #content,ytm-browse[has-side-navigation] #page-manager{margin-left:0!important;padding-left:0!important;width:100%!important}ytd-two-column-browse-results-renderer,ytd-two-column-search-results-renderer{grid-template-columns:minmax(0,1fr)!important;column-gap:0!important}';
            function resetYoutubeLeftGap(){var root=document.scrollingElement||document.documentElement;if(root&&root.scrollLeft!==0)root.scrollLeft=0;if(document.body&&document.body.scrollLeft!==0)document.body.scrollLeft=0;document.querySelectorAll('ytm-browse[has-side-navigation],ytm-app[has-side-navigation]').forEach(function(el){el.removeAttribute('has-side-navigation');});['ytm-navigation-drawer-renderer','ytm-guide-renderer','ytm-mini-guide-renderer','ytd-mini-guide-renderer','ytd-guide-renderer','tp-yt-app-drawer','#guide','#guide-content','#secondary'].forEach(function(sel){document.querySelectorAll(sel).forEach(function(el){if(el.style.getPropertyValue('display')!=='none')el.style.setProperty('display','none','important');});});['#page-manager','ytd-page-manager','#columns','#content','#contents','ytm-browse','ytm-search','ytm-app','ytm-section-list-renderer','ytm-rich-grid-renderer','ytm-item-section-renderer'].forEach(function(sel){document.querySelectorAll(sel).forEach(function(el){['margin-left','padding-left','left'].forEach(function(p){if(el.style.getPropertyValue(p)!=='0px'&&el.style.getPropertyValue(p)!=='0')el.style.setProperty(p,'0','important');});if(el.style.getPropertyValue('max-width')!=='100%')el.style.setProperty('max-width','100%','important');});});}
            resetYoutubeLeftGap();if(!window.__appYoutubeGapObserver){window.__appYoutubeGapObserver=new MutationObserver(resetYoutubeLeftGap);window.__appYoutubeGapObserver.observe(document.documentElement,{childList:true,subtree:true,attributes:true,attributeFilter:['class','has-side-navigation']});}window.scrollTo(0,0);
           })();
          """.trimIndent(),null)
          view?.scrollTo(0,0)
         }
        }
        player.webChromeClient=WebChromeClient()
        musicWebView=player
        player.loadUrl(youtubeSearchUrl)
       }
      },
      update={ view -> if(view.url!=youtubeSearchUrl) view.loadUrl(youtubeSearchUrl) }
     )
     Column(
      modifier=Modifier.align(Alignment.BottomEnd).padding(8.dp),
      verticalArrangement=Arrangement.spacedBy(6.dp),
      horizontalAlignment=Alignment.CenterHorizontally
     ) {
      FilledTonalButton(
       onClick={
        musicWebView?.evaluateJavascript("""
         (function(){
          var amount=Math.max(window.innerHeight*0.85,500);
          window.dispatchEvent(new WheelEvent('wheel',{deltaY:-amount,bubbles:true,cancelable:true}));
          Array.from(document.querySelectorAll('*')).filter(function(e){var s=getComputedStyle(e);return /(auto|scroll)/.test(s.overflowY)&&e.scrollHeight>e.clientHeight+40;}).forEach(function(e){e.scrollTo({top:0,behavior:'smooth'});});
          if(document.scrollingElement)document.scrollingElement.scrollTo({top:0,behavior:'smooth'});
          window.scrollTo({top:0,behavior:'smooth'});
          document.dispatchEvent(new KeyboardEvent('keydown',{key:'ArrowUp',code:'ArrowUp',bubbles:true}));
         })();
        """.trimIndent(),null)
       },
       contentPadding=PaddingValues(horizontal=12.dp,vertical=5.dp),
       modifier=Modifier.height(38.dp)
      ) { Text("↑",fontSize=19.sp,fontWeight=FontWeight.Bold) }
      FilledTonalButton(
       onClick={
        musicWebView?.evaluateJavascript("""
         (function(){
          var amount=Math.max(window.innerHeight*0.85,500);
          window.dispatchEvent(new WheelEvent('wheel',{deltaY:amount,bubbles:true,cancelable:true}));
          var candidates=Array.from(document.querySelectorAll('*')).filter(function(e){var s=getComputedStyle(e);return /(auto|scroll)/.test(s.overflowY)&&e.scrollHeight>e.clientHeight+40;});
          candidates.sort(function(a,b){return (b.scrollHeight-b.clientHeight)-(a.scrollHeight-a.clientHeight);});
          candidates.forEach(function(e){e.scrollBy({top:amount,behavior:'smooth'});});
          if(document.scrollingElement)document.scrollingElement.scrollBy({top:amount,behavior:'smooth'});
          window.scrollBy({top:amount,behavior:'smooth'});
          document.dispatchEvent(new KeyboardEvent('keydown',{key:'ArrowDown',code:'ArrowDown',bubbles:true}));
         })();
        """.trimIndent(),null)
       },
       contentPadding=PaddingValues(horizontal=12.dp,vertical=5.dp),
       modifier=Modifier.height(38.dp)
      ) { Text("↓",fontSize=19.sp,fontWeight=FontWeight.Bold) }
     }
    }
   }
  }
 }
Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
  OutlinedButton(onClick={showSettings=true;play(ToneGenerator.TONE_PROP_BEEP)},modifier=Modifier.weight(1f)){Text("⚙",fontSize=17.sp)}
  OutlinedButton(onClick={
  updateStatus=extraText("checking",selectedLanguage)
  scope.launch {
   try {
    val result=withContext(Dispatchers.IO) {
     val connection=URL("https://api.github.com/repos/shasivarimehdat075-glitch/pogodi-gi-zborovite/releases/tags/latest").openConnection()
     connection.setRequestProperty("Accept","application/vnd.github+json")
     connection.setRequestProperty("User-Agent","PogodiGiZborovite-Android")
     connection.connectTimeout=15000; connection.readTimeout=15000
     val json=JSONObject(connection.getInputStream().bufferedReader().use{it.readText()})
     val latest=json.optString("name")
     val assets=json.getJSONArray("assets")
     var apkUrl=""
     for(i in 0 until assets.length()) { val asset=assets.getJSONObject(i); if(asset.optString("name")=="app-release.apk") apkUrl=asset.optString("browser_download_url") }
     Pair(latest,apkUrl)
    }
    val current=context.packageManager.getPackageInfo(context.packageName,0).versionName ?: "0"
    fun parts(v:String)=v.split(".").map{it.toIntOrNull()?:0}
    val latestParts=parts(result.first); val currentParts=parts(current)
    val newer=(0 until maxOf(latestParts.size,currentParts.size)).firstNotNullOfOrNull { i -> val a=latestParts.getOrElse(i){0}; val b=currentParts.getOrElse(i){0}; if(a!=b) a>b else null } ?: false
    if(result.first.isBlank() || result.second.isBlank()) updateStatus=extraText("no_release",selectedLanguage)
    else if(!newer) updateStatus=extraText("latest",selectedLanguage)+" ($current)."
    else { updateStatus=extraText("found",selectedLanguage)+" ${result.first}"; onUpdate(result.second) }
   } catch(e:Exception) { updateStatus=extraText("failed",selectedLanguage) }
  }
 },modifier=Modifier.weight(1f)){Text("🔄 "+extraText("update",selectedLanguage),fontSize=12.sp,maxLines=1,softWrap=false)}
  OutlinedButton(onClick={showExitDialog=true},modifier=Modifier.weight(1f)){Text("🚪 "+uiText("exit",selectedLanguage))}
 }
 if(updateStatus.isNotBlank()) Text(updateStatus,color=Color(0xFFFFD782),textAlign=TextAlign.Center,fontSize=13.sp)
 if(selected<0){
  Text(when(selectedLanguage){"fr"->"CHOISIS UN JEU";"sq"->"ZGJIDH NJË LOJË";"en"->"CHOOSE A GAME";else->"ИЗБЕРИ ИГРА"},color=mainText,fontSize=20.sp,fontWeight=FontWeight.Bold)
  val games=listOf(
   "🧱" to listOf("Tetris","Tetris","Tetris","Тетрис"),
   "🐍" to listOf("Snake","Snake","Snake","Змија"),
   "🧱" to listOf("Casse-briques","Thyes tullash","Brick Breaker","Арканоид"),
   "🔢" to listOf("2048","2048","2048","2048"),
   "💣" to listOf("Démineur","Minëpastrues","Minesweeper","Минер"),
   "🧠" to listOf("Jeu de mémoire","Lojë kujtese","Memory Match","Меморија"),
   "⭕" to listOf("Morpion","Tic-tac-toe","Tic-tac-toe","Икс-точка"),
   "🔣" to listOf("Sudoku","Sudoku","Sudoku","Судоку"),
   "🐤" to listOf("Vol d'oiseau","Fluturimi i zogut","Bird Flight","Лет на птица"),
   "🏃" to listOf("Course infinie","Vrapim pa fund","Endless Runner","Бескрајно трчање"),
   "🫧" to listOf("Bubble Shooter","Bubble Shooter","Bubble Shooter","Меурчиња"),
   "🔴" to listOf("Puissance 4","Katër në rresht","Connect Four","Четири во низа"),
   "🧩" to listOf("Puzzle","Puzzle","Jigsaw Puzzle","Сложувалка"),
   "♟️" to listOf("Échecs","Shah","Chess","Шах"),
   "🎨" to listOf("Tri des couleurs","Renditja e ngjyrave","Color Sort","Подредување бои")
  )
  games.forEachIndexed { index,item ->
   Card(onClick={if(index==1){selected=1;level=0;answer="";message=""}else{selected=2+index}},colors=CardDefaults.cardColors(containerColor=panel),shape=RoundedCornerShape(16.dp),modifier=Modifier.fillMaxWidth()){
    Row(Modifier.fillMaxWidth().padding(16.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)){
     Text(item.first,fontSize=30.sp)
     Text(item.second[when(selectedLanguage){"fr"->0;"sq"->1;"en"->2;else->3}],color=mainText,fontWeight=FontWeight.Bold,fontSize=18.sp,modifier=Modifier.weight(1f))
     Text("›",color=Color(0xFF8DE1D2),fontSize=28.sp)
    }
   }
  }
 } else if(selected==1){
  var snake by remember { mutableStateOf(listOf(7 to 7,6 to 7,5 to 7)) }
  var food by remember { mutableStateOf(10 to 7) }
  var direction by remember { mutableStateOf(1 to 0) }
  var nextDirection by remember { mutableStateOf(1 to 0) }
  var snakeScore by remember { mutableIntStateOf(0) }
  var snakeOver by remember { mutableStateOf(false) }
  var snakeRunning by remember { mutableStateOf(true) }
  fun resetSnake(){snake=listOf(7 to 7,6 to 7,5 to 7);food=10 to 7;direction=1 to 0;nextDirection=1 to 0;snakeScore=0;snakeOver=false;snakeRunning=true}
  LaunchedEffect(selected,snakeRunning,snakeOver) {
   while(selected==1 && snakeRunning && !snakeOver){
    delay(230)
    direction=nextDirection
    val head=snake.first()
    val newHead=(head.first+direction.first) to (head.second+direction.second)
    if(newHead.first !in 0..13 || newHead.second !in 0..13 || newHead in snake.dropLast(1)){snakeOver=true}
    else{
     val ate=newHead==food
     snake=if(ate) listOf(newHead)+snake else listOf(newHead)+snake.dropLast(1)
     if(ate){snakeScore+=10;var candidate=(kotlin.random.Random.nextInt(14) to kotlin.random.Random.nextInt(14));while(candidate in snake) candidate=(kotlin.random.Random.nextInt(14) to kotlin.random.Random.nextInt(14));food=candidate}
    }
   }
  }
  Text("🐍 Snake",color=mainText,fontSize=22.sp,fontWeight=FontWeight.Bold)
  Text((if(selectedLanguage=="fr")"Score" else if(selectedLanguage=="sq")"Pikët" else if(selectedLanguage=="en")"Score" else "Поени")+": $snakeScore",color=gold,fontWeight=FontWeight.Bold,fontSize=18.sp)
  androidx.compose.foundation.layout.Box(Modifier.fillMaxWidth().aspectRatio(1f).background(Color(0xFF14251F),RoundedCornerShape(12.dp)).padding(5.dp)){
   Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(2.dp)){
    (0..13).forEach { y -> Row(Modifier.weight(1f),horizontalArrangement=Arrangement.spacedBy(2.dp)){
     (0..13).forEach { x ->
      val cell=x to y
      Box(Modifier.weight(1f).fillMaxHeight().background(if(cell==food) Color(0xFFFF5959) else if(cell in snake) Color(0xFF62D99B) else Color(0xFF203B32),RoundedCornerShape(2.dp)))
     }
    }}
   }
  }
  if(snakeOver) Text(if(selectedLanguage=="fr")"Partie terminée !" else if(selectedLanguage=="sq")"Loja mbaroi!" else if(selectedLanguage=="en")"Game over!" else "Крај на играта!",color=Color(0xFFFF7777),fontWeight=FontWeight.Bold)
  @androidx.compose.runtime.Composable
  fun directionButton(label:String,dx:Int,dy:Int){OutlinedButton(onClick={if(!(direction.first+dx==0 && direction.second+dy==0))nextDirection=dx to dy},modifier=Modifier.size(64.dp)){Text(label,fontSize=22.sp)}}
  Column(horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(5.dp)){
   directionButton("⬆",0,-1)
   Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){directionButton("⬅",-1,0);directionButton("⬇",0,1);directionButton("➡",1,0)}
  }
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
   Button(onClick={snakeRunning=!snakeRunning; if(snakeOver)resetSnake()}){Text(if(snakeRunning) (if(selectedLanguage=="fr")"Pause" else "Pause") else (if(selectedLanguage=="fr")"Reprendre" else if(selectedLanguage=="en")"Resume" else if(selectedLanguage=="sq")"Vazhdo" else "Продолжи"))}
   OutlinedButton(onClick={resetSnake()}){Text(if(selectedLanguage=="fr")"Recommencer" else if(selectedLanguage=="sq")"Rifillo" else if(selectedLanguage=="en")"Restart" else "Почни повторно")}
  }
  TextButton(onClick={selected=-1}){Text(uiText("back_categories",selectedLanguage))}
 } else if(selected==2){
  val shapes=listOf(listOf(0 to 0,1 to 0,2 to 0,3 to 0),listOf(0 to 0,0 to 1,1 to 1,2 to 1),listOf(2 to 0,0 to 1,1 to 1,2 to 1),listOf(1 to 0,2 to 0,1 to 1,2 to 1),listOf(1 to 0,2 to 0,0 to 1,1 to 1),listOf(1 to 0,0 to 1,1 to 1,2 to 1),listOf(0 to 0,1 to 0,1 to 1,2 to 1))
  var board by remember { mutableStateOf(List(18){List(10){0}}) }
  var piece by remember { mutableStateOf(shapes.random()) }
  var px by remember { mutableIntStateOf(3) }; var py by remember { mutableIntStateOf(0) }
  var ticks by remember { mutableIntStateOf(0) }; var tetrisOver by remember { mutableStateOf(false) }; var tetrisScore by remember { mutableIntStateOf(0) }
  fun fits(s:List<Pair<Int,Int>>,x:Int,y:Int):Boolean=s.all{(a,b)->x+a in 0..9 && y+b in 0..17 && board[y+b][x+a]==0}
  fun spawn(){piece=shapes.random();px=3;py=0;if(!fits(piece,px,py))tetrisOver=true}
  fun settle(){
   val next=board.map{it.toMutableList()}
   piece.forEach{(a,b)->if(py+b in 0..17 && px+a in 0..9)next[py+b][px+a]=1}
   val kept=next.filter{row->row.any{it==0}}
   val cleared=18-kept.size
   board=List(cleared){List(10){0}}+kept
   tetrisScore+=cleared*100
   spawn()
  }
  LaunchedEffect(selected,tetrisOver){
   while(selected==2 && !tetrisOver){delay(550);if(fits(piece,px,py+1))py++ else settle()}
  }
  Text("🧱 Tetris",color=mainText,fontSize=22.sp,fontWeight=FontWeight.Bold)
  Text("Score : $tetrisScore",color=gold,fontSize=18.sp,fontWeight=FontWeight.Bold)
  val display=board.map{it.toMutableList()}.also{grid->piece.forEach{(a,b)->if(py+b in 0..17 && px+a in 0..9)grid[py+b][px+a]=2}}
  Column(Modifier.fillMaxWidth().aspectRatio(10f/18f).background(Color(0xFF101827),RoundedCornerShape(8.dp)).padding(3.dp),verticalArrangement=Arrangement.spacedBy(2.dp)){
   display.forEach { row ->
    Row(Modifier.weight(1f), horizontalArrangement=Arrangement.spacedBy(2.dp)) {
     row.forEach { v ->
      val cellColor = when(v) { 1 -> Color(0xFF46B7A4); 2 -> Color(0xFFFFC857); else -> Color(0xFF26344B) }
      Box(Modifier.weight(1f).fillMaxHeight().background(cellColor, RoundedCornerShape(2.dp)))
     }
    }
   }
  }
  if(tetrisOver)Text(if(selectedLanguage=="fr")"Partie terminée !" else "Game over!",color=Color(0xFFFF7777),fontWeight=FontWeight.Bold)
  Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
   OutlinedButton(onClick={if(fits(piece,px-1,py))px--}){Text("◀")}
   OutlinedButton(onClick={val rotated=piece.map{(x,y)->(y to (2-x))};if(fits(rotated,px,py))piece=rotated}){Text("↻")}
   OutlinedButton(onClick={if(fits(piece,px+1,py))px++}){Text("▶")}
   Button(onClick={while(fits(piece,px,py+1))py++;settle()}){Text("⬇")}
  }
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
   Button(onClick={board=List(18){List(10){0}};tetrisScore=0;tetrisOver=false;spawn()}){Text(if(selectedLanguage=="fr")"Recommencer" else "Restart")}
   TextButton(onClick={selected=-1}){Text(uiText("back_categories",selectedLanguage))}
  }
 } else {
  Text(when(selectedLanguage){"fr"->"Ce jeu arrive bientôt";"sq"->"Kjo lojë vjen së shpejti";"en"->"This game is coming soon";else->"Оваа игра доаѓа наскоро"},color=mainText,fontSize=20.sp)
  TextButton(onClick={selected=-1}){Text(uiText("back_categories",selectedLanguage))}
 }
 Text(uiText("tagline",selectedLanguage),color=secondaryText,fontSize=13.sp)
 }
 Surface(Modifier.align(Alignment.BottomCenter).fillMaxWidth(),color=panel,shadowElevation=8.dp) {
  Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal=22.dp,vertical=12.dp),horizontalArrangement=Arrangement.Center,verticalAlignment=Alignment.CenterVertically) {
   Text("🏆  ${uiText("total_points",selectedLanguage)}: $score",color=gold,fontWeight=FontWeight.ExtraBold,fontSize=18.sp)
  }
 }
 }
 if(showSettings) {
  AlertDialog(onDismissRequest={showSettings=false},title={Text(uiText("settings",selectedLanguage))},text={Column {
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text(extraText("dark_light",selectedLanguage));Switch(checked=darkMode,onCheckedChange={darkMode=it;settings.edit().putBoolean("dark_mode",it).apply()})}
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text(extraText("game_sounds",selectedLanguage));Switch(checked=soundsOn,onCheckedChange={soundsOn=it;settings.edit().putBoolean("sounds_on",it).apply();if(it)playGameSound(context,ToneGenerator.TONE_PROP_ACK,true,soundVolume)})}
   Text(extraText("effects_volume",selectedLanguage)+"${(soundVolume*100).toInt()}%",fontSize=13.sp)
   Slider(value=soundVolume,onValueChange={soundVolume=it;settings.edit().putFloat("sound_volume",it).apply()},valueRange=0f..1f)
   Text(extraText("volume_help",selectedLanguage),fontSize=13.sp)
  }},confirmButton={TextButton(onClick={showSettings=false}){Text(uiText("done",selectedLanguage))}})
 }
 if(showExitDialog) {
  AlertDialog(onDismissRequest={showExitDialog=false},
   title={Text(extraText("exit_title",selectedLanguage))},
   text={Text(extraText("exit_confirm",selectedLanguage))},
   confirmButton={TextButton(onClick={showExitDialog=false;onExit()}){Text(uiText("exit",selectedLanguage))}},
   dismissButton={TextButton(onClick={showExitDialog=false}){Text(uiText("cancel",selectedLanguage))}}
  )
 }
 }
}
