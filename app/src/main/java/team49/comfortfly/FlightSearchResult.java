package team49.comfortfly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.model.LegInfo;
import com.google.api.services.qpxExpress.model.PassengerCounts;
import com.google.api.services.qpxExpress.model.SegmentInfo;
import com.google.api.services.qpxExpress.model.SliceInfo;
import com.google.api.services.qpxExpress.model.SliceInput;
import com.google.api.services.qpxExpress.model.TripOption;
import com.google.api.services.qpxExpress.model.TripOptionsRequest;
import com.google.api.services.qpxExpress.model.TripsSearchRequest;
import com.google.api.services.qpxExpress.model.TripsSearchResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlightSearchResult extends AppCompatActivity {

    ListView resultListView;
    String originLatLng;
    String destinationLatLng;
    String departDate;
    List<Trip> list;

//    Double[] coefMonth = new Double[]{
//            6.42437005e+07,  6.42437005e+07,  6.42437005e+07,  6.42437005e+07,
//            6.42437005e+07,  6.42437005e+07,  1.05688103e+11,  -5.49463103e+10,
//            -1.55176233e+10,  -6.39467432e+11,  -3.20301830e+10,  -1.11517921e+11 };
//    Double coef = 3.29234913e+07-2.10841165e+08+2.28685169e+08+4.63586917e+07+2.42087761e+09
//            +3.16515103e+09+3.59457863e+07;
    Double[] coef = new Double[] {
        0.00019667833988, 0.000928658103703, -1.03080232973e-05, -0.00121638898011, -0.00131025827078, 0.00141161882108, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.00060775401545, 0.000317613586748, 0.000381110636739, 0.00376533266847, 0.000241812404614, 0.000170273690184, -0.000815034083363, 0.00180205504112, -0.00168075760715, 0.000697139135313, -0.000669381898232, 0.0010723220572, -0.00125557921779, -0.00302312602137, -0.0018103586027, 0.000642624418865, 0.000387744039268, -9.65027139999e-05, 0.000785314817079, -0.00186864745078, 0.000570468655927, 0.00020411118437, -0.00119707766687, -0.00360000934044, 0.000219566592174, -0.000182710417383, 0.00208647380513, 8.83256641652e-05, -0.00141800159585, -0.0019400926264, 0.00673274488285, -0.000393341561116, 0.00127860084831, -0.00108179699108, -0.00101353084974, 0.000701808925939, -0.000102904280499, 0.000611163915348, -0.00598413688268, -0.00498379501009, -0.00421442836832, 0.00893271019536, 0.00101252095371, 0.0, -0.0056470614689, -0.00692506799461, -0.00148776134609, -0.0129324478165, 0.00492313767433, 0.00721800208214, 0.00397023149813, 0.00298448942615, 0.00310161634607, 0.0015899158883, 0.00158823003883, 0.000937516431954, 0.000448897528832, -0.000231398634583, 1.41050719022e-05, -0.000524209646279, -5.86902450236e-06, 0.000369921710962, 0.000603848519554, 0.000599884719849, 0.000685761361931, 0.000365179698351, 0.000815643263493, 0.000752308684671, 0.000523472730869, 0.000523916006712, 0.000584097753711, 0.000893309181115, 0.00111385373851, 0.000970819053368, 0.00149241328908, 0.00128366091008, 0.00168157559214, 0.000928297744557, 0.0012277078069, 0.00019477004772, -7.25432511681e-05, -0.000438986870726, -0.00133147727693, -0.000488908663909, -0.00262458321944, -0.00443913947643, 0.000214708207058, 0.000210995080371, -0.000168488400848, 0.000388032548962, 0.00131908022564, -0.00343834968147, -0.000859713574448, -0.00302874065826, 0.00205353830121, 0.00016004884973, 0.00443900581353, 0.00294717212922, 0.000569602217558, 0.00188134194083, 0.000744450186764, 0.000882335371633, -2.11718644697e-05, -0.00218599698245, -0.0019587064704, -0.00237338889852, -0.0017969957902, -0.00185583105149, -0.00197619225018, -0.00116724565344, -0.00165254240151, -0.00149681659766, -0.00159732717322, -0.00182356033467, -0.000847888445225, -0.00129356646985, -0.000855045273343, 4.27367936752e-05, -5.48019696969e-05, 9.79460038902e-05, 0.000824346053345, 0.000981709083839, 0.000931546043137, 0.000666380460792, 0.00101849723678, 0.000915329791701, 0.000877982228361, 0.000812772966475, 0.00113838953613, 0.00129672479523, 0.00160188248578, 0.00167007065517, 0.00101400294948, 0.000751741982635, -0.00321324848431, 0.000850646869262, 0.00402490795861, 8.98192281989e-05, 0.000888130624784, 0.00118538265325, -0.00144868627974, -0.000455665765052, 0.00091833137799, -0.00560037562222, -0.000309024572981, 0.00231144139117, -0.00144340319306, 0.000720911218531, 0.00243127695518, 0.000410987293158, -0.0028686491628, 0.00171848792176, 0.00182741673802, -0.00203868712737, -0.00409940695919, -0.00161832140689, 1.78708072365e-05, -0.00286555189058, 0.00111468546123, -8.43558125675e-05, -0.000278592894382, -0.00517797372674, -0.000312071097449, -0.00247409119258, -0.000766784543257, -0.00178153954015, -0.00184521202234, -0.00425006265672, -0.00296631059054, -0.000577219179767, 6.5570721061e-06, 0.00109612849371, 0.00432499223498, -0.0016249129603, 0.004131382755, 4.32046257524e-05, -5.69875691652e-06, -0.00213893140521, 0.00899340974271, 0.000760207903163, -0.00145768831271, -0.00424693189624, -0.00291932676223, 0.00367929031634, -0.00153971555271, -0.00220840178922, 0.00296834814781, -0.000310447423749, -0.000626039571129, 0.00330884414167, -0.00040454294448, -0.000638545862343, 0.00126709594062, 0.00511190895681, -0.00141662434003, -0.00284948348484, -0.00117558335484, 0.000153818535134, 0.00238524119388, 0.000807160449019, -0.000467622457164, 0.000556181499843, 0.00171853067814, 0.000434059891129, 0.00191200903878, -5.28664182052e-05, -0.000449816232838, -0.001823569711, 0.000805673633512, -0.000965717090983, 0.00339638536519, 0.0185239121502, 0.0037186729886, 0.00127088748983, 0.00106929593124, 0.000862776213384, -0.000866887978616, -0.00188350877179, -0.00126937187578, -0.000545748620425, -0.00181030384811, 0.000871693427347, -0.00111110005182, -0.00396127183296, -0.0020827320892, 0.0011164348784, 0.00101431034135, -0.00480707252908, 0.0040123823092, -0.00099598786174, -0.00129680997052, 0.00664549751964, -0.00421298927602, 0.00614120254318, -0.00090698358161, -0.00129424270967, 0.00149414692479, 0.0077380163082, 0.0488991578759, -0.00320042014944, -0.00098597890144, -0.00165216155528, -0.00370911923158, 0.000170417324362, -0.00149736630275, 0.00230040822947, -0.00499579358023, 0.000241013048434, 7.61912058843e-05, -0.00256436189402, -0.00540389125348, -0.000675927193879, -0.000705128058235, -0.00181352718368, -0.00263237871268, -0.0036779121728, 0.00198328066555, 0.00384276718139, 0.00463353073739, -0.00200839639956, -0.0026361711592, -0.000367055106929, -0.00145181377926, -0.000660670268534, 0.000894303568055, 0.000992173668768, 0.000758613099561, 0.00382750441863, -0.00100183455941, -0.00333491588921, 0.00299748283881, -0.00101095786346, 0.000255321181945, -0.00696783371822, -0.0019336387682, -0.00399080829951, -0.00135798568638, -0.00231290743074, 0.00350410715755, -0.0186466649007, -0.00249369496686, -0.00395862265706, 0.00199011634851, -0.00309077458743, -0.00157083606151, 0.000408314733542, 0.00174414331445, 0.000888580256545, -0.00400731346105, 0.00115780585919, -0.00665178475735, -0.00271791054792, -0.00235042246158, -0.00498988981672, -0.0113455844253, -0.00173301095043, -0.00422299597735, -0.00363577061878, -0.0032781243432, -0.00332759930034, -0.00360933368052, -0.00224692946414, -0.00422033859781, -0.00240157565985, -0.00205216344608, -0.00486368731209, -0.0015208457664, -0.00436966380703, -0.00344177053255, -0.00183238111215, -0.00312726335482, 0.000906263761275, -0.00297236612206, -0.00388789504328, -0.00503957006151, -0.0013984975022, 0.0107780498219, 0.0042837657914, 0.00202527629511, 0.00907314958045, -0.00518770698408, -0.0145320344022, 0.000478123149974, -0.00167222311986, 0.00560073713789, 0.00151567335573, -0.00500397719118, 0.00116679818876, 0.0026254853538, 6.06795914525e-05, -0.00416664290503, -0.00497092110117, 0.00202075759276, -0.0025012066683, 1.7824865167e-05, -0.00283433915759, 0.00332942521638, -0.00426839743391, -0.00728599599813, -0.00654398117711, -0.00567637785012, -0.00388183617248, -0.00161152102031, 0.00116638875931, -0.000487291879695, -0.000802727654312, -0.000141422408652, 0.00301134016475, -0.00364461023872, -0.0010039316348, 0.0032810603133, -9.35387565416e-05, 0.00796569022771, -0.00142702172327, 0.000918753514584, 0.00226523281253, 0.00375010747874, 0.00424999706563, -0.00024447734239, 0.000837801011767, -0.000561863570208, 0.000588473711368, -0.00210363591072, 0.00406798368738, 0.00315579667406, -0.00249494105719, 0.00822997736489, -0.00234112774309, 0.00200971066544, -0.00156360379857, 0.00231205067215, -0.00176496380455, 0.000344394950429, 0.0402662285595, -0.0001725001208, 0.000326147849785, -0.000486763534238, -0.00589653355184, -0.00551543480026, -0.00322765819299, 0.00180039170459, 0.00553324585102, 0.00104261776028, 0.00061037719976, -0.000885622563965, 0.000843305974132, 0.0142987135818, 0.00716067487099, 0.00492515894156, 0.00991090388054, -0.00106518863768, -0.00286991351935, 0.00659722718626, 0.00122076817405, -0.00213518123402, -0.00155901662622, -0.00113271498622, 0.000432703179783, 0.00225934124917, 0.00532286902849, 0.00954459703466, -0.0056256151413, -0.00235872974168, 0.0204111338832, -0.00359782876383, -0.00117481836218, -0.0063572350059, -0.000707900855468, 0.00320567946147, -0.00236252403332, -0.00210351461926, 0.000773985089022, 0.0014368230923, -0.00371322411276, -0.00130013992095, 0.00318894824726, -0.00866783194912, 0.0069647544251, 0.000532319842165, -0.00458777470138, 0.00411428620082, -0.00402581380167, 0.00541856287935, 0.00368030495322, 0.00588220036048, -0.00207500931365, 0.00432784324991, -0.00894335075681, -0.0057583626956, 0.00618984510898, -0.000678067082217, -1.27506143445e-05, 0.00885944870186, -0.0017851924819, 0.00940688210949, -0.00265339206655, 0.00712054227669, -0.0102128618026, -9.76554601516e-05, -0.0112417179201, -0.000659410352997, -0.00278671854942, 0.00201151469814, 0.00870947466494, 0.00530906755637, 0.00519073307585, -0.00206540718418, -0.0114291388217, -0.0101437994613, 0.000343332646564, 0.00136610054577, 0.00256530603367, 0.0012616730054, 0.000373872053185, 0.00088275714885, 0.00179584926279, -0.00065868960768, -0.001425096194, -0.00198940143949, -0.000141130472121, 0.00166107232929, -0.00103395837009, -2.11999834704e-05, -0.000400172303185, -0.000310417695202, -9.35921977386e-05, -5.78348574818e-05, -0.00307407972793, 0.00222001761208, 9.14421613704e-05, 0.000664962017312, -0.00034761157699, -0.000103240781767, -0.00178299839508, 0.00120154422072, -0.000276091942019, 0.000124166329251, 0.000100485802508, -0.000965212150646, 0.00446768859794, -1.3284747847e-05, -0.000703423946358, -0.00150380500715, 5.91978204775e-05, 0.00113768745057, 0.0241107606539, 0.00173715673695, 0.00191659454133, 0.00193572417669, 0.000356572393513, -0.0012749756092, 0.00194410113857, -0.000288468769816, -0.000532510003831, 0.000210162544077, 7.37415927316e-05, -0.000175265344458, -0.00111782143899, 0.000705114794109, -0.000398335805998, -0.000792708606109, 0.0018910003934, -0.000652904359477, 0.000146657305217, 0.00135386091444, 0.000644096262259, 0.000940843790049, 0.00284002418184, -0.00147824155532, 0.000663154345317, 0.00485321537322, -0.000732145679538, -0.00493629082541, 0.00142187197383, -0.000723296556241, 0.000797275709149, 0.00164271538231, -0.00233101812755, 0.000475411332833, 0.00266209401402, -0.00107642820749, -0.000592904946707, 0.000267727264584, -0.000499448617096, -0.000757426742695, -0.00140484087835, 8.62125489381e-05, -0.0011660111818, 0.000984873386531, -0.000110035733106, 0.00136041941217, 0.000540590669333, -0.000286066515837, -0.00178359163575, 0.00166563754954, -0.000563710465933, -0.000600566966918, 0.000318002980934, -0.000747261454173, -0.00156358967808, -0.000484507422242, -0.00212681877672, 0.00110669735859, 0.00501417084357, 0.00163233860265, -0.00220919923825, 0.000296978077742, 0.000933241971729, -0.00268169600759, -7.63618682256e-05, 0.00012100316501, -0.00024499614976, -0.000235982195643, 0.000250357208351, 0.00145692274457, 0.000644604891089, -0.000499843501874, 0.000972596718031, 0.00180447303505, 0.00829818268021, -0.000824866148737, 0.00123765847496, 0.000562977264654, 0.000596291975001, 0.000570398325601, -0.000571104890591, 0.00218154310509, -0.000188446500818, 0.00325712236923, -0.000616055651113, -0.000915785729904, -0.00848440760435, 0.00187666727452, -0.000542411256518, 0.00458902619471, -7.71288972532e-05, 0.00167023306112, -0.000185786362376, 0.00517436463763, 0.00816302527229, 0.00819551208842, -0.00133612579415, 0.00158432215088, 0.00257227640872, 0.000158220773823, -0.0053990228632, 0.000550489858991, -0.000784470129302, -0.00721628162325, -0.000364733897452, -0.00163011942005, -0.00319443447565, -0.00196926783968, -0.000939820392402, -0.000671583120088, -0.000634611287482, 0.000278545421048, -0.00142464346244, -0.000616130518814, -0.00109278045053, -0.00135027254803, -0.00276829491607, -0.000339045960389, -0.000127725737716, -0.00130937625681, 0.00037142204438, -0.00167427340459, -0.00122307277274, 0.00336607004825, -0.000852260361632, -0.00220159332788, 0.00032400648454, 0.00106541793168, -0.0020133240183, -0.0129195900968, -0.00773106460127, -0.000653170796525, -0.00243043529503, 0.00624182538968, -0.00629722195664, 4.95320892693e-05, -0.00397230102272, -0.0019421860539, -0.00107571141489, -0.00355619462633, -0.00587644315786, -0.00446021023039, -9.56246050878e-05, 0.00307629877809, -0.00380796931349, 0.000970524966116, -0.00189796659389, -0.00438585857012, -0.00388183617248, -0.00157153069098, 0.00161200018444, 0.000301113984384, 0.00346536402512, 0.00111462550169, -0.000840370909539, 0.000123105409635, -0.0020043664495, 0.00119003155244, 0.00231781346219, 0.0064881358586, 0.0028158669502, -0.000202808850666, 0.00138729682635, 0.000156555077591, 0.00294382342266, 0.00567719974131, 0.00290655477735, -0.00243389958793, -0.00178395044259, 0.0015494721266, 0.00100434516445, -0.000389686369732, 0.00267638055015, 0.0035265774059, 0.000562950082899, 0.0021436490542, 0.000388216136337, 0.00117825246826, -0.000207769166301, 0.00270703385499, 0.00119255604079, -0.000724378456237, 0.0027468679181, 0.00502062361877, -0.00283960381781, -0.0014784536875, -0.000612948409448, 0.00207645640464, 0.00901500905, 0.000863550131729, 0.00715945340218, -0.00101182507561, -0.00255208817385, -0.00249814825246, -0.000830111261483, -0.00125161857339, 0.000993117264213, 0.000189194163942, -0.00114803672776, -0.000179856439049, -0.00301624958337, -0.00191572158497, 0.000245642608323, 0.000497659740974, -0.000429417591144, -0.000806649171758, -0.0017732813597, 0.00142361265492, -0.00289349851647, 0.019197587462, -0.00130644701297, -0.00182866532821, -0.00166365422575, -0.00261884179926, 0.000126863854364, -0.003754670007, -0.00509906714725, 0.000617965136289, 0.000222915103781, -0.00208245418142, -0.000184632097746, -0.000404948032266, 0.000357576702664, 0.00112923680697, 0.000139365683287, -0.000730600857, -0.00349370948184, -0.00330443182217, -0.00153906727699, 0.00471167735598, -0.00160194192007, -0.00219262078361, -0.00444825368566, -0.00418975552465, 0.00327550109851, 0.00335346719848, -0.00565496378579, 0.00256619653009, -0.00730208190555, -0.00361940130636, -0.00497938014841, -0.00447652304245, 0.00410584716609, -0.00835646076603, -0.00430476238155, -0.000256572156346, 0.00369119814363, -0.00298702474308, -0.00271263613628, -0.00746412831669, 0.000555077296219, 0.00254306134051, 0.000243603274537, 0.00465388423818, 0.00555956287065, -7.87599406452e-05, 0.00769151885901, -0.00487460197823, -0.00300343946581};
    String[] carrier = new String[] {"9E",
            "AA",
            "AQ",
            "AS",
            "B6",
            "CO",
            "DL",
            "EV",
            "F9",
            "FL",
            "HA",
            "MQ",
            "NW",
            "OH",
            "OO",
            "UA",
            "US",
            "WN",
            "XE",
            "YV"};

    String[] airport = new String[] {
            "ATL",
            "HOU",
            "SRQ",
            "IAH",
            "XNA",
            "GSP",
            "CID",
            "MSP",
            "GRB",
            "SUX",
            "ORF",
            "DLH",
            "MCI",
            "DTW",
            "MEM",
            "MSN",
            "IDA",
            "IAD",
            "MBS",
            "BIL",
            "FSD",
            "TYS",
            "LIT",
            "STL",
            "GFK",
            "LGA",
            "DSM",
            "RAP",
            "RST",
            "TVC",
            "OMA",
            "BNA",
            "FWA",
            "CMH",
            "GRR",
            "MDT",
            "AVL",
            "GSO",
            "LAN",
            "SCE",
            "SWF",
            "HSV",
            "PIT",
            "DAY",
            "SDF",
            "AVP",
            "JAN",
            "BUF",
            "ABE",
            "HPN",
            "SBN",
            "PWM",
            "RIC",
            "FNT",
            "BGR",
            "CLE",
            "LNK",
            "PLN",
            "BGM",
            "AZO",
            "MLI",
            "CAE",
            "OKC",
            "DCA",
            "CHS",
            "ALB",
            "IND",
            "BTV",
            "SAV",
            "ERI",
            "TUL",
            "CAK",
            "ROC",
            "CVG",
            "PIA",
            "BHM",
            "MYR",
            "FCA",
            "CPR",
            "ALO",
            "BZN",
            "FLL",
            "FAR",
            "SGF",
            "CMX",
            "HLN",
            "RSW",
            "RDU",
            "SLC",
            "BDL",
            "BOS",
            "PHL",
            "BIS",
            "ICT",
            "GTF",
            "RHI",
            "LSE",
            "SHV",
            "TLH",
            "PNS",
            "JAX",
            "CLT",
            "GPT",
            "CHA",
            "FSM",
            "MOB",
            "AUS",
            "PFN",
            "EVV",
            "ATW",
            "ELM",
            "CWA",
            "ROA",
            "LEX",
            "MGM",
            "DFW",
            "BTR",
            "BWI",
            "SYR",
            "STX",
            "MKE",
            "DEN",
            "CRW",
            "SAT",
            "BJI",
            "INL",
            "COS",
            "LAX",
            "JFK",
            "OGG",
            "HNL",
            "SFO",
            "EWR",
            "ORD",
            "SAN",
            "SJU",
            "KOA",
            "MIA",
            "LAS",
            "SEA",
            "LIH",
            "MCO",
            "SNA",
            "TUS",
            "ABQ",
            "ELP",
            "BUR",
            "STT",
            "ONT",
            "MSY",
            "FAT",
            "PSP",
            "PBI",
            "PHX",
            "SJC",
            "TPA",
            "PDX",
            "MFE",
            "RNO",
            "SMF",
            "OAK",
            "MTJ",
            "EGE",
            "HDN",
            "JAC",
            "GUC",
            "ANC",
            "ITO",
            "KTN",
            "GEG",
            "JNU",
            "SIT",
            "LGB",
            "PSG",
            "CDV",
            "YAK",
            "BET",
            "BRW",
            "SCC",
            "FAI",
            "BOI",
            "ADQ",
            "WRG",
            "OME",
            "OTZ",
            "ADK",
            "DLG",
            "AKN",
            "PSE",
            "BQN",
            "MDW",
            "DAB",
            "PVD",
            "VPS",
            "MLB",
            "PHF",
            "FAY",
            "AGS",
            "GNV",
            "ABY",
            "DHN",
            "TRI",
            "OAJ",
            "AEX",
            "EWN",
            "MEI",
            "CRP",
            "GRK",
            "ILM",
            "GTR",
            "LFT",
            "LYH",
            "HHH",
            "EYW",
            "VLD",
            "CSG",
            "MHT",
            "ISP",
            "MLU",
            "ACY",
            "BMI",
            "MCN",
            "CHO",
            "TOL",
            "FLO",
            "BQK",
            "LAW",
            "LWB",
            "SBP",
            "SBA",
            "MRY",
            "SPS",
            "ABI",
            "CLL",
            "TYR",
            "GGG",
            "ACT",
            "SJT",
            "TXK",
            "LRD",
            "DAL",
            "LBB",
            "MAF",
            "AMA",
            "CMI",
            "ROW",
            "MQT",
            "DBQ",
            "GJT",
            "MOT",
            "MSO",
            "SUN",
            "SGU",
            "ASE",
            "EUG",
            "MOD",
            "DRO",
            "MFR",
            "PSC",
            "YUM",
            "RDM",
            "RDD",
            "CLD",
            "SPI",
            "TWF",
            "SMX",
            "ACV",
            "BFL",
            "CEC",
            "CIC",
            "PMD",
            "EKO",
            "IYK",
            "OXR",
            "IPL",
            "PIH",
            "BTM",
            "BLI",
            "RFD",
            "COD",
            "SLE",
            "LWS",
            "CDC",
            "YKM",
            "MKG",
            "WYS",
            "HRL",
            "BRO",
            "LCH",
            "BPT",
            "ACK",
            "FLG",
            "TEX",
            "RKS",
            "GCC"};
    /*
            3.29234913e+07, //DayOfMonth
            -2.10841165e+08, //DayOfWeek
            2.28685169e+08 //DepTime
            4.63586917e+07 //ArrTime
            2.42087761e+09
            3.16515103e+09
            3.59457863e+07

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search_result);
        resultListView = (ListView) findViewById(R.id.resultListView);
        list = new ArrayList<>();
        Intent intent = getIntent();
        originLatLng = intent.getExtras().getString("originLatLng");
        destinationLatLng = intent.getExtras().getString("destinationLatLng");
        departDate = intent.getExtras().getString("departDate");
        new GoogleFlightSearch().execute();
    }

    class GoogleFlightSearch extends AsyncTask<Void, Boolean, Boolean> {

        private static final String APPLICATION_NAME = "MyFlightApplication";
        private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        List<TripOption> tripResults;
        private HttpTransport httpTransport;

        @Override
        protected Boolean doInBackground(Void... params) {

            String originAirport;
            String destinationAirport;

            try {
                /* Get departure airport code */
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://iatageo.com/getCode/" + originLatLng.split(",")[0] +
                        "/" + originLatLng.split(",")[1]);
                HttpResponse response = httpclient.execute(httpget);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    originAirport = responseString.split("\"")[7];
                } else {
                    return false;
                }

                /* Get destination airport code */
                httpget = new HttpGet("http://iatageo.com/getCode/" + destinationLatLng.split(",")[0] +
                        "/" + destinationLatLng.split(",")[1]);
                response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    destinationAirport = responseString.split("\"")[7];
                } else {
                    return false;
                }

                httpTransport = new ApacheHttpTransport();

                SliceInput slice1 = new SliceInput();
                slice1.setMaxStops(3);
                slice1.setOrigin(originAirport);
                slice1.setDestination(destinationAirport);
                slice1.setDate(departDate);

                List<SliceInput> slices = new ArrayList<>();
                slices.add(slice1);

                TripOptionsRequest request = new TripOptionsRequest();
                request.setSolutions(20);
                request.setPassengers(new PassengerCounts().setAdultCount(1));
                request.setSlice(slices);

                TripsSearchRequest parameters = new TripsSearchRequest();
                parameters.setRequest(request);
                QPXExpress qpXExpress = new QPXExpress.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
                        .setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(Credential.API_KEY)).build();

                TripsSearchResponse list = qpXExpress.trips().search(parameters).execute();
                this.tripResults = list.getTrips().getTripOption();
                if (this.tripResults == null || this.tripResults.size() == 0)
                    return false;
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return false;
            } catch (Throwable t) {
                t.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                final List<Trip> result = new LinkedList<>();
                for (TripOption t : this.tripResults) {

                    // Populate the data into the template view
                    SliceInfo sliceInfo = t.getSlice().get(0);
                    Double delayTime = 0.0;
                    SimpleDateFormat departureDate;
                    String[] departureDateSplit;
                    String[] TimeSplit;
                    for (SegmentInfo seg : sliceInfo.getSegment()) {
                        for (LegInfo leg : seg.getLeg()) {
                            Trip trip = new Trip();
                            Double perDelayTime = 0.0;
                            String departTime = leg.getDepartureTime();
                            String arrivalTime = leg.getArrivalTime();

                            trip.DepartureDate = departTime.substring(0, 10);
                            trip.ArrivalDate = arrivalTime.substring(0, 10);
                            trip.DepartureTime = departTime.substring(11, 16);
                            trip.ArrivalTime = arrivalTime.substring(11, 16);
                            trip.Origin = leg.getOrigin();
                            trip.Destination = leg.getDestination();

                            trip.Airline = seg.getFlight().getCarrier();
                            trip.FlightNumber = seg.getFlight().getNumber();
                            trip.Duration = "";
                            result.add(trip);

                            departureDateSplit = trip.DepartureDate.split("-");

                            System.out.println(departTime);
                            int idx = departTime.indexOf("T");
                            TimeSplit = departTime.substring(idx+1).split(":");
                            int minute = Integer.parseInt(TimeSplit[1].split("-")[0]) < 30 ? 0:1;
                            perDelayTime += coef[Integer.parseInt(departureDateSplit[1]) -1];
                            perDelayTime += coef[12 + Integer.parseInt(departureDateSplit[2]) -1];
                            perDelayTime += coef[12 + 31 +
                                    Integer.parseInt(TimeSplit[0])*2 + minute -1];

                            System.out.println(arrivalTime);
                            idx = arrivalTime.indexOf("T");
                            TimeSplit = arrivalTime.substring(idx+1).split(":");
                            minute = Integer.parseInt(TimeSplit[1].split("-")[0]) < 30 ? 0:1;
                            System.out.println(TimeSplit[0]);
                            System.out.println(minute);

                            perDelayTime += coef[12 + 31 + 48 +
                                    Integer.parseInt(TimeSplit[0])*2 + minute -1];
                            for( int i = 0; i < carrier.length; i++) {
                                if( trip.Airline.equals(carrier[i])) {
                                    perDelayTime += coef[12 + 31 + 48 + 48 + i];
                                    break;
                                }
                            }
                            for( int i = 0; i < airport.length; i++) {
                                if( trip.Origin.equals(airport[i])) {
                                    perDelayTime += coef[12 + 31 + 48 + 48 + 20 + i];
                                }
                                if( trip.Destination.equals(airport[i])) {
                                    perDelayTime += coef[12 + 31 + 48 + 48 + 20 + 295 + i];
                                }
                            }
                            delayTime += 2467 * perDelayTime;
                        }
                    }
                    Trip tripp = new Trip();
                    StringBuilder sb = new StringBuilder();

                    int duration = sliceInfo.getDuration();
                    if (duration > 60) {
                        sb.append(duration / 60);
                        sb.append("h ");
                    }
                    sb.append(duration % 60);
                    sb.append("m");

                    tripp.Duration = sb.toString();
                    tripp.Price = t.getPricing().get(0).getSaleTotal();
                    tripp.Delay = "Delay:" + String.valueOf(delayTime.intValue()) + "m";

                    result.add(tripp);
                }
                System.out.println("Test");
                for (Trip t : result) {
                    System.out.println(t.DepartureTime);
                    System.out.println("D" + t.Duration);
                }

                TripDetailAdapter s = new TripDetailAdapter(getApplicationContext(), result);
                resultListView.setAdapter(s);

                resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Trip trip = (Trip) parent.getItemAtPosition(position);
                        if (!trip.Price.equals("")) {

                            position--;
                            while (position > 0 && result.get(position).Price.equals("")) {
                                System.out.println(result.get(position));
                                list.add(result.get(position));
                                position--;
                            }

                            final AlertDialog.Builder builder = new AlertDialog.Builder(FlightSearchResult.this);
                            builder.setTitle("");
                            builder.setMessage("Do you want to add this itinerary to your trips?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new SendNewItinerary().execute(list.toArray(new Trip[list.size()]));
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            builder.create().show();
                        }
                    }
                });

            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(FlightSearchResult.this);
                builder.setTitle("Alert");
                builder.setMessage("No result found!");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                builder.create().show();
            }
        }
    }

    class SendNewItinerary extends AsyncTask<Trip, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Trip... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://fa17-cs411-49.cs.illinois.edu/api/trip?token=" + Home.token);

            try {

                for (Trip trip : params) {
                    httppost.setEntity(new StringEntity("{\"action\":\"insert\"," + trip.toString() + "}"));
                    HttpResponse response = httpclient.execute(httppost);
                    System.out.println(response.getStatusLine().getStatusCode());
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String responseString = EntityUtils.toString(response.getEntity());
                        System.out.println(responseString);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
    }
}
