package ru.starfarm.client.mixins.font;

import lombok.val;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.render.ClientFont;

import java.util.Locale;
import java.util.Random;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {

    @Shadow
    private boolean unicodeFlag;

    @Shadow
    protected abstract float renderDefaultChar(int ch, boolean italic);

    @Shadow
    protected abstract float renderUnicodeChar(char ch, boolean italic);

    @Shadow
    protected float posX;

    @Shadow
    protected float posY;

    @Shadow
    protected abstract void enableAlpha();

    @Shadow
    protected abstract void resetStyles();

    @Shadow
    protected abstract int renderString(String text, float x, float y, int color, boolean dropShadow);

    @Shadow
    public abstract int getCharWidth(char character);

    @Shadow
    private boolean randomStyle;

    @Shadow
    private boolean boldStyle;

    @Shadow
    private boolean strikethroughStyle;

    @Shadow
    private boolean underlineStyle;

    @Shadow
    private boolean italicStyle;

    @Shadow
    @Final
    private int[] colorCode;

    @Shadow
    private int textColor;

    @Shadow
    protected abstract void setColor(float r, float g, float b, float a);

    @Shadow
    private float alpha;

    @Shadow
    private float red;

    @Shadow
    private float blue;

    @Shadow
    private float green;

    @Shadow
    public Random fontRandom;

    @Shadow
    protected abstract void doDraw(float f);

    @Inject(at = @At("HEAD"), method = "renderChar", cancellable = true)
    public void renderChar(char ch, boolean italic, CallbackInfoReturnable<Float> cir) {
        if (ClientFont.isClientChar(ch)) cir.setReturnValue(renderChar(ch, italic, false));
    }

    public float renderChar(char ch, boolean italic, boolean shadow) {
        if (ch == 160 || ch == ' ') return 4.0F;
        else if (ClientFont.isClientChar(ch)) {
            if (!shadow) {
                val texture = ClientFont.getTexture(ch);
                val renderer = StarClient.instance().overlayRenderer();
                texture.bind();
                renderer.isolateMatrix(() -> {
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    renderer.drawScaledCustomSizeModalRect(
                            (int) posX, (int) posY, 0, 0,
                            texture.width(), texture.height(), 9, 9, texture.width(), texture.height()
                    );
                    GlStateManager.disableBlend();
                });
            }

            return 9f;
        } else {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(ch);
            return i != -1 && !this.unicodeFlag ? this.renderDefaultChar(i, italic) : this.renderUnicodeChar(ch, italic);
        }
    }

    @Inject(at = @At("HEAD"), method = "getCharWidth", cancellable = true)
    public void getCharWidth(char ch, CallbackInfoReturnable<Integer> cir) {
        if (ClientFont.isClientChar(ch)) cir.setReturnValue(9);
    }

    @Inject(at = @At("HEAD"), method = "drawString(Ljava/lang/String;FFIZ)I", cancellable = true)
    public void drawString(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        text = ClientFont.resolveEmojis(text);
        enableAlpha();
        resetStyles();

        cir.setReturnValue(dropShadow
                ? Math.max(renderString(text, x + 1.0F, y + 1.0F, color, true),
                renderString(text, x, y, color, false))
                : renderString(text, x, y, color, false)
        );
    }

    @Inject(at = @At("HEAD"), method = "getStringWidth", cancellable = true)
    public void getStringWidth(String text, CallbackInfoReturnable<Integer> cir) {
        if (text == null) cir.setReturnValue(0);
        else {
            text = ClientFont.resolveEmojis(text);
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < text.length(); ++j) {
                char c0 = text.charAt(j);
                int k = getCharWidth(c0);

                if (k < 0 && j < text.length() - 1) {
                    ++j;
                    c0 = text.charAt(j);

                    if (c0 != 'l' && c0 != 'L') {
                        if (c0 == 'r' || c0 == 'R') {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }

                    k = 0;
                }

                i += k;

                if (flag && k > 0) {
                    ++i;
                }
            }

            cir.setReturnValue(i);
        }
    }

    @Inject(at = @At("HEAD"), method = "renderStringAtPos", cancellable = true)
    public void renderStringAtPos(String text, boolean shadow, CallbackInfo ci) {
        ci.cancel();
        for (int i = 0; i < text.length(); ++i)
        {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length())
            {
                int i1 = "0123456789abcdefklmnor".indexOf(String.valueOf(text.charAt(i + 1)).toLowerCase(Locale.ROOT).charAt(0));

                if (i1 < 16)
                {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;

                    if (i1 < 0 || i1 > 15)
                    {
                        i1 = 15;
                    }

                    if (shadow)
                    {
                        i1 += 16;
                    }

                    int j1 = this.colorCode[i1];
                    this.textColor = j1;
                    setColor((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
                }
                else if (i1 == 16)
                {
                    this.randomStyle = true;
                }
                else if (i1 == 17)
                {
                    this.boldStyle = true;
                }
                else if (i1 == 18)
                {
                    this.strikethroughStyle = true;
                }
                else if (i1 == 19)
                {
                    this.underlineStyle = true;
                }
                else if (i1 == 20)
                {
                    this.italicStyle = true;
                }
                else if (i1 == 21)
                {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    setColor(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            }
            else
            {
                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

                if (this.randomStyle && j != -1)
                {
                    int k = this.getCharWidth(c0);
                    char c1;

                    while (true)
                    {
                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == this.getCharWidth(c1))
                        {
                            break;
                        }
                    }

                    c0 = c1;
                }

                float f1 = j == -1 || this.unicodeFlag ? 0.5f : 1f;
                boolean flag = (c0 == 0 || j == -1 || this.unicodeFlag) && shadow;

                if (flag)
                {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderChar(c0, this.italicStyle, false);

                if (flag)
                {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle)
                {
                    this.posX += f1;

                    if (flag)
                    {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.renderChar(c0, this.italicStyle, true);
                    this.posX -= f1;

                    if (flag)
                    {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }
                doDraw(f);
            }
        }
    }


}
