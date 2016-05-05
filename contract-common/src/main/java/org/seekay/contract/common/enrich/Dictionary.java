package org.seekay.contract.common.enrich;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Dictionary {

  public static String randomWord() {
    Random random = new Random();
    return words.get(random.nextInt(words.size()));
  }

  private static final List<String> words = new ArrayList<String>() {{
    addAll(Arrays.asList("condition","guarantee","trip","activity","aspiring","hospitable","naughty","disapprove",
        "lunchroom","frame","appreciate","thirsty","glistening","dapper","trick","intelligent","abortive","secretary",
        "silent","treatment","use","hushed","risk","neat","panoramic","allow","nasty","uttermost","itch","equal",
        "fierce","error","fallacious","plot","learned","meal","lick","vessel","kindly","stupid","afford","psychotic",
        "motionless","acoustics","sheep","ruddy","apathetic","marble","drawer","futuristic","bedroom","name","play",
        "best","man","coil","sip","tangible","comfortable","self","six","harmonious","welcome","hang","smelly",
        "flawless","spy","petite","object","expand","tedious","bashful","twig","feeble","wise","leather","tacky",
        "early","leg","pack","bear","calculator","doll","advice","overflow","macho","squash","well-groomed","prevent",
        "jazzy","hot","writing","wait","puzzled","army","fade","brawny","brief","retire","economic","increase",
        "hilarious","stone","eminent","disgusting","gleaming","laugh","meeting","madly","incredible","bore","endurable",
        "suspend","modern","fairies","plant","lyrical","typical","rambunctious","plantation","afternoon","shop","sink",
        "unhealthy","sky","zephyr","incompetent","helpful","pail","yell","deceive","pin","painstaking","discover",
        "wrathful","wide","belong","well-made","pig","plant","perpetual","whip","wacky","island","possible","toys",
        "tooth","ratty","discreet","brash","chew","sound","large","love","snake","drop","radiate","melted","grateful",
        "fresh","club","optimal","driving","interfere","spot","clever","fantastic","light","guide","produce","knee",
        "juvenile","notice","clean","joke","bee","electric","copper","thunder","frogs","squeeze","squirrel","cars",
        "check","agonizing","parched","mitten","smoke","curvy","soap","icicle","paint","huge","use","cannon","roof",
        "spoon","act","lazy","matter","bulb","mother","temporary","load","notebook","harsh","holistic","oafish",
        "tomatoes","crush","third","vein","curious","obedient","value","alert","wail","dinosaurs","pumped","announce",
        "print","hammer","quickest","move","ocean","trap","transport","noisy","untidy","bat","earth","place",
        "scribble","water","magical","boy","cream","voyage","careful","quilt","continue","glossy","guttural","step",
        "bikes","glass","join","instruct","dock","zipper","ladybug","tour","spotted","ink","volleyball","questionable",
        "interesting","count","fuzzy","suck","rigid","tricky","vague","chubby","bed","spiders","buzz","penitent","rule",
        "unequal","embarrassed","natural","adaptable","travel","sad","hill","possess","chunky","colorful","enormous",
        "point","prefer","proud","approve","heady","trousers","bad","grip","sidewalk","nail","gabby","icky","illegal",
        "accurate","separate","confess","heartbreaking","striped","nervous","occur","simple","scream","existence",
        "waste","eight","quirky","bumpy","idiotic","verdant","obese","force","faithful","remain","belief","towering",
        "plane","cub","dad","escape","need","bottle","harmony","box","lake","seat","sign","shave","part","street",
        "maddening","dangerous","paper","strong","noxious","sore","settle","force","turn","search","reproduce","books",
        "birth","tasteless","pine","waiting","jelly","dust","debt","move","tough","correct","square","flash","recess",
        "actually","healthy","fanatical","x-ray","aunt","science","quizzical","request","secretive","hulking","suspect",
        "butter","hope","knot","uncle","charge","ludicrous","stream","letter","toy","grass","sand","measly","curly",
        "subtract","admire","effect","broad","callous","time","license","stiff","whirl","fire","fluttering","burly",
        "reward","doubt","number","mass","strange","amount","increase","education","careless","apparel","fang",
        "acceptable","look","calendar","thaw","moaning","jump","stamp","wandering","untidy","range","volcano","hair",
        "cumbersome","reason","memorise","curve","nerve","drink","giant","laborer","massive","behave","clover","stem",
        "precede","thin","oranges","trot","unadvised","price","terrible","trains","savory","little","float","fax",
        "decisive","wine","tiresome","slap","delirious","kick","cabbage","irritate","hum","charming","salt","blind",
        "frail","grouchy","slimy","dramatic","afterthought","mix","smile","gate","needless","explain","second","aloof",
        "call","structure","insurance","dare","shaggy","egg","snakes","channel","rejoice","one","flesh","undress",
        "ship","taste","productive","alike","crash","learn","switch","dysfunctional","helpless","sock","shut","sister",
        "cheer","small","juicy","imminent","mouth","label","current","excuse","waves","top","meddle","worthless",
        "efficient","question","society","bow","ready","introduce","snow","smart","nonstop","rich","real","panicky",
        "fix","shiver","far","tray","punish","disturbed","frightening","cave","makeshift","delicious","scene","rose",
        "cowardly","accessible","sisters","safe","attack","wrap","serious","terrific","pollution","trucks","lettuce",
        "admit","round","special","superficial","snails","absent","entertaining","comparison","obtainable","own",
        "ambitious","ethereal","reminiscent","wink","exotic","polish","support","noiseless","energetic","brass",
        "willing","taste","wilderness","toothsome","need","zesty","battle","chin","cute","legs","ugly","fair","note",
        "crime","example","wooden","gold","numberless","muddled","playground","wanting","exuberant","youthful",
        "laughable","cheat","different","shock","arrive","steam","witty","cooing","flower","hesitant","lock","sassy",
        "new","friendly","nimble","park","desire","tightfisted","wide-eyed","box","push","gruesome","scatter","evasive",
        "rabid","dime","caption","decide","pizzas","border","authority","pot","breakable","colossal","talented",
        "dazzling","childlike","loaf","sin","open","care","abiding","amusement","acrid","satisfying","pretty","birds",
        "suggest","black-and-white","didactic","toad","fear","coherent","vegetable","furtive","coal","stitch","normal",
        "verse","wonderful","tank","evanescent","tug","elbow","fork","male","cattle","plough","improve","rotten","cats",
        "late","explode","scorch","puffy","dogs","tasty","scandalous","full","tick","wood","soft","left","quaint",
        "responsible","summer","opposite","invention","useless","humor","rabbits","mammoth","mysterious","visitor",
        "obey","parcel","word","color","quiet","try","tested","quarter","drop","stroke","note","oil","drab",
        "unaccountable","glue","faded","half","event","sweater","baby","roll","jog","blood","jobless","suit","fill",
        "arrest","things","ill","believe","courageous","upset","zany","undesirable","mice","turn","hurry","telling",
        "necessary","fluffy","bat","trees","woozy","jumpy","poor","torpid","wonder","rural","flap","rainy","alleged",
        "false","accept","farm","pancake","plausible","bell","versed","magic","receptive","pause","bounce","anxious",
        "glove","free","squealing","rain","yielding","pipe","vengeful","three","stereotyped","kiss","fold","skillful",
        "waggish","excited","living","floor","great","gamy","muscle","direful","multiply","steadfast","scientific",
        "orange","judge","ignore","cakes","romantic","fancy","prickly","year","distribution","jam","umbrella",
        "addition","chivalrous","afraid","ear","launch","wrong","punishment","orange","seal","peace","carriage",
        "return","therapeutic","scold","crabby","grab","husky","known","test","agree","common","shoe","trace",
        "permissible","car","whistle","rat","entertain","rail","hook","wish","crack","observation","pleasure",
        "hysterical","abaft","far-flung","key","homely","camp","interest","oven","grip","smiling","fireman","mushy",
        "robust","glamorous","sour","functional","coordinated","class","arithmetic","curved","frog","nostalgic","dolls",
        "abundant","jail","holiday","picture","defeated","humorous","ubiquitous","straw","squalid","wasteful","clumsy",
        "building","abject","dispensable","strap","tent","sugar","rub","pickle","number","snore","detail","ruin"));
  }};
}
