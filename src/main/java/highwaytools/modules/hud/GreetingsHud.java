package highwaytools.modules.hud;

import highwaytools.HighwayTools;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.hud.elements.TextHud;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.NameProtect;

import java.util.Calendar;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class GreetingsHud extends HudElement {
    public static final HudElementInfo<GreetingsHud> INFO = new HudElementInfo<>(HighwayTools.HUD, "greetings-hud", "Displays a friendly welcome.", GreetingsHud::new);

    public enum Mode {
        HIGTools,
        Time
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("What text to show for the greeting.")
        .defaultValue(Mode.Time)
        .build()
    );


    public GreetingsHud() {
        super(INFO);
    }


    private String leftText;
    private String rightText;

    private double leftWidth;


    @Override
    public void tick(HudRenderer renderer) {
        Calendar calendar = Calendar.getInstance();
        int localTime = calendar.get(Calendar.HOUR_OF_DAY);

        if (mode.get() == Mode.HIGTools) {
            leftText = "Welcome to HIG Tools";
        } else {
            if (localTime <= 12) leftText = "Good Morning";
            if (localTime >= 13 && localTime <= 16) leftText = "Good Afternoon";
            if (localTime >= 17) leftText = "Good Evening";
        }

        leftText = leftText + ", ";
        rightText = Modules.get().get(NameProtect.class).getName(mc.getSession().getUsername());

        leftWidth = renderer.textWidth(leftText);
        double rightWidth = renderer.textWidth(rightText);

        box.setSize((leftWidth + rightWidth), renderer.textHeight());
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = this.x;
        double y = this.y;

        if (isInEditor()) {
            renderer.text("GreetingsHud", x, y, TextHud.getSectionColor(0), true);
            return;
        }

        renderer.text(leftText, x, y, TextHud.getSectionColor(0), true);
        renderer.text(rightText, x + leftWidth, y, TextHud.getSectionColor(1), true);
    }
}