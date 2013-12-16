package org.levental.yelp.it;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.levental.yelp.cli.ConcurrentPipeline;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.levental.yelp.it.JapeRulesTest.TextExpectation.txt;

@RunWith(Parameterized.class)
public class JapeRulesTest {
    private static final String SIMPLE_REVIEW_1 = "Best food, super friendly staff, and great prices. Love it!";
    private static final String SIMPLE_REVIEW_2 = "The deli sandwiches are good.  I usually stick to getting the Amy's turkey-O. " +
            "If you plan on substituting a side, make sure you tell them because the cashiers don't normally ask and you'll be stuck with chips.  " +
            "The salad bar here is very decent for the price.";
    private static final String SIMPLE_REVIEW_3 = "Do yourself a favor and get their Bloody Mary. It was phenomenal and simply the best I've ever had.  " +
            "I'm pretty sure they only use ingredients from their garden and blend them fresh when you order it.  It was amazing.";
    private static final String SIMPLE_REVIEW_4 = "love the gyro plate. Rice is so good and I also dig their candy selection :)";
    private static final String SIMPLE_REVIEW_5 = "Food is great, but service is too slow. I eat soup. It was very nice";
    private static final String SIMPLE_REVIEW_6 = "Five star for the food";
    private static final String SIMPLE_REVIEW_7 = "Not busy at all but took nearly 45 min to get our meal.  " +
            "Ordered the trout and was shocked to see lots and lots of bones. Hmmmmm. Well asked the waitress about it and she said " +
            "\"they try the best they can\"  hmmmmmm isn't this a \"fish\" restaurant? They comped the trout but still not sure I would go back.";

    private static final String COMPLEX_REVIEW_1 = "My wife took me here on my birthday for breakfast and it was excellent.  " +
            "The weather was perfect which made sitting outside overlooking their grounds an absolute pleasure.  " +
            "Our waitress was excellent and our food arrived quickly on the semi-busy Saturday morning.  " +
            "It looked like the place fills up pretty quickly so the earlier you get here the better.";
    private static final String COMPLEX_REVIEW_2 = "Rosie, Dakota, and I love Chaparral Dog Park. " +
            "It's very convenient and surrounded by a lot of paths, a desert xeriscape, baseball fields, ballparks, " +
            "and a lake with ducks. The Scottsdale Park and Rec Dept. does a wonderful job of keeping the park clean and shaded.  " +
            "You can find trash cans and poopy-pick up mitts located all over the park and paths." +
            "The fenced in area is huge to let the dogs run, play, and sniff!";
    private static final String COMPLEX_REVIEW_3 = "Drop what you're doing and drive here. After I ate here " +
            "I had to go back the next day for more.  The food is that good. This cute little green building may " +
            "have gone competely unoticed if I hadn't been driving down Palm Rd to avoid construction.  " +
            "While waiting to turn onto 16th Street the 'Grand Opening' sign caught my eye and my little yelping " +
            "soul leaped for joy!  A new place to try! It looked desolate from the outside but when I opened the door " +
            "I was put at easy by the decor, smell and cleanliness inside.  I ordered dinner for two, to go.  " +
            "The menu was awesome.  I loved seeing all the variety: poblano peppers, mole, " +
            "mahi mahi, mushrooms...something wrapped in banana leaves.  It made it difficult to choose something.  " +
            "Here's what I've had so far: La Condesa Shrimp Burro and Baja Sur Dogfish Shark Taco.  They are both were " +
            "very delicious meals but the shrimp burro stole the show.  So much flavor.  I snagged some bites from my " +
            "hubbys mole and mahi mahi burros- mmmm such a delight.  The salsa bar is endless.  I really stocked up.  " +
            "I was excited to try the strawberry salsa but it was too hot, in fact it all was, but I'm a big wimp when it " +
            "comes to hot peppers. The horchata is handmade and delicious.  They throw pecans and some fruit in there too " +
            "which is a yummy bonus! As if the good food wasn't enough to win me over the art in this restaurant sho did!  " +
            "I'm a sucker for Mexican folk art and Frida Kahlo is my Oprah.  There's a painting of her and Diego hanging over " +
            "the salsa bar, it's amazing.  All the paintings are great, love the artist.";
    private static final String COMPLEX_REVIEW_4 = "I had not been to an Oregano's in like 10 years. They seems to be popping " +
            "up all over here in AZ now so my buddy was in from out of town and he wanted to try, so why not. I'll tell you \"Why NOT\"... " +
            "we has two thin crust pizzas... and I even hate to call them that. More like... crackers with melted cheese and some toppings." +
            "That was some of the worst excuse for pizza I've ever had. Most of the fast-food delivery pizza I've had beats it -- and " +
            "I do not like, at all, delivery pizza. Maybe the pan pizza is better than the thin crust... god it could not be worse!" +
            "It only deserves one star for the good service... other than that is the another sub-par overpriced \"gourmet\" pizza joint." +
            "If you want expensive, and good, pizza there are many good alternatives... Pizzeria Bianco, Grimaldi's, etc.";
    private static final String COMPLEX_REVIEW_5 = "U can go there n check the car out. If u wanna buy 1 there? That's wrong move! " +
            "If u even want a car service from there? U made a biggest mistake of ur life!! " +
            "I had 1 time asked my girlfriend to take my car there for an oil service, guess what? " +
            "They ripped my girlfriend off by lying how bad my car is now. If without fixing the problem. " +
            "Might bring some serious accident. Then she did what they said. 4 brand new tires, timing belt, 4 new brake pads. " +
            "U know why's the worst? All of those above I had just changed 2 months before!!! What a trashy dealer is that? " +
            "People, better off go somewhere!";
    private static final String COMPLEX_REVIEW_6 = "Disgusting!  Had a Groupon so my daughter and I tried it out.  " +
            "Very outdated and gaudy 80's style interior made me feel like I was in an episode of Sopranos.  " +
            "The food itself was pretty bad.  We ordered pretty simple dishes but they just had no flavor at all!  " +
            "After trying it out I'm positive all the good reviews on here are employees or owners creating them.";
    private static final String COMPLEX_REVIEW_7 = "Here's the 1. 2. 3... 1. Great Food. I love hot New Mexico style food. " +
            "Good job. 2. They have a little brewery and make 3 house beers. " +
            "Unfortunately, they're all shitty. 3. They could REALLY improve the service. No Bueno! We'll be back for the food. " +
            "It might be take out next time though! 4-17-11 Update: Some asshole who was clearly affiliated with Arribas " +
            "contacted me via private message to slam me for my review. I hope the yelp police find you and pull you " +
            "off the site. More importantly, I hope more people read my review and stay away from this restaurant. " +
            "Good Food folks, just get it to go! To Bryan L. and the rest of the gang over at Arribas....Go EFF yourselves! " +
            "You get one star and NO more of my money!!!!!!! Learn how to work, Kid!";

    private static final String ddd = "I saw a car. It was beautiful";

    private ConcurrentPipeline annotator;
    private TextExpectation expectation;

    @Before
    public void setUp() throws Exception {
        annotator = new ConcurrentPipeline();
    }

    @Parameterized.Parameters
    public static List<TextExpectation[]> parameters() {
        return ImmutableList.<TextExpectation[]>of(
                txt(SIMPLE_REVIEW_1).ex("food", "best").ex("staff", "friendly").ex("prices", "great").arr(),
                txt(SIMPLE_REVIEW_2).ex("deli sandwiches", "good").arr(),
                txt(SIMPLE_REVIEW_3).ex("bloody mary", "phenomenal").arr(),
                txt(SIMPLE_REVIEW_4).ex("Rice", "good").arr(),
                txt(SIMPLE_REVIEW_5).ex("food", "great").ex("service", "slow").ex("soup", "nice").arr(),
                txt(SIMPLE_REVIEW_6).ex("food", "five star").arr(),
                txt(SIMPLE_REVIEW_7).ex("food", "five star").arr(),
                txt(COMPLEX_REVIEW_1).ex("waitress", "excellent").arr(),
                txt(COMPLEX_REVIEW_2).ex("Chaparral Dog Park", "convenient").arr(),
                txt(COMPLEX_REVIEW_3).ex("food", "good").ex("place", "new").ex("menu", "awesome").ex("meals", "delicious").ex("salsa bar", "endless")
                        .ex("horchata", "handmade").ex("paintings", "great").arr(),
                txt(COMPLEX_REVIEW_4).ex("excuse", "worst").ex("service", "one star").arr(),
                txt(COMPLEX_REVIEW_5).ex("move", "wrong").ex("mistake", "biggest").ex("accident", "serious").arr(),
                txt(COMPLEX_REVIEW_6).ex("food", "bad").ex("dishes", "simple").ex("reviews", "good").arr(),
                txt(COMPLEX_REVIEW_7).ex("food", "New Mexico").ex("job", "good").ex("brewery", "little").arr()
        );
    }

    @Test
    public void test() throws Exception {
        expectation.assertText(annotator.execute(expectation.text));
    }

    public JapeRulesTest(TextExpectation expectation) {
        this.expectation = expectation;
    }

    public static class TextExpectation {
        private Text text;
        private List<String> objAnnotations = new ArrayList<String>();
        private List<String> adjectives = new ArrayList<String>();

        public TextExpectation(String txt) {
            this.text = new Text("unknown", txt);
        }

        public static TextExpectation txt(String txt) {
            return new TextExpectation(txt);
        }

        public TextExpectation ex(String object, String adjective) {
            objAnnotations.add(object);
            adjectives.add(adjective);
            return this;
        }

        public TextExpectation[] arr() {
            return new TextExpectation[]{this};
        }

        public void assertText(Text txt) {
            assertEquals(txt.getText(), text.getText());
            System.err.println(printTextDebugInformation(txt, AnnotationType.Property, StaticFeature.adjective));
            Iterable<Annotation> actualObjects = txt.getByType(AnnotationType.Property);

            for (int i = 0; i < objAnnotations.size(); i++) {
                String expectedObject = objAnnotations.get(i);
                Annotation actualObject = Iterables.find(actualObjects, str(expectedObject), null);
                assertNotNull(String.format("Object '%s' wasn't extracted from '%s' \nbut extracted: %s",
                        expectedObject,
                        printTextDebugInformation(txt, AnnotationType.Token, StaticFeature.category),
                        printTextDebugInformation(txt, AnnotationType.Property, StaticFeature.adjective)
                ),
                        actualObject
                );
                String expectedAdj = adjectives.get(i).toLowerCase(Locale.ENGLISH);
                String actualAdj = String.valueOf(actualObject.getFeature(StaticFeature.adjective)).toLowerCase(Locale.ENGLISH);
                assertEquals(String.format("Object \'%s\' has wrong adjective: expected '%s', actual '%s'", expectedObject, expectedAdj, actualAdj), expectedAdj,
                        actualAdj);
            }
        }

        private String printTextDebugInformation(Text txt, AnnotationType type, StaticFeature feature) {
            StringBuilder sb = new StringBuilder();
            for (Annotation each : txt.getByType(type)) {
                sb.append(txt.getText(each)).append("_").append(each.getFeature(feature)).append(" ");
            }
            return sb.toString();
        }

        private Predicate<Annotation> str(final String str) {
            return new Predicate<Annotation>() {
                @Override
                public boolean apply(Annotation input) {
                    return str.equalsIgnoreCase(text.getText(input));
                }
            };
        }
    }
}
