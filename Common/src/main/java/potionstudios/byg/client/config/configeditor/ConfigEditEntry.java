package potionstudios.byg.client.config.configeditor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import potionstudios.byg.client.config.ScreenPosition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigEditEntry<T> extends ContainerObjectSelectionList.Entry<ConfigEditEntry<T>> {

    public final Screen parent;
    public final String key;
    public final int maxKeyWidth;
    public final int maxCommentWidth;
    private final Component comment;
    protected final ScreenPosition keyScreenPosition = new ScreenPosition();
    public final List<Component> toolTip = new ArrayList<>();
    private int cachedWidth = 0;
    public boolean renderToolTip;

    public ConfigEditEntry(Screen parent, String key) {
        this(parent, key, new TextComponent(""));
    }

    public ConfigEditEntry(Screen parent, String key, String comment) {
        this(parent, key, new TextComponent(comment));
    }

    public ConfigEditEntry(Screen parent, String key, Component comment) {
        this.parent = parent;
        this.key = key;
        this.maxKeyWidth = Minecraft.getInstance().font.width(key);
        this.maxCommentWidth = Minecraft.getInstance().font.width(comment);
        this.comment = comment;
    }

    public void render(PoseStack pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
        float x = (float) ((pLeft) - (this.getRowWidth())) + pWidth;
        float y = (float) (pTop + pHeight / 2 - 9 / 2);

        this.keyScreenPosition.x = x;
        this.keyScreenPosition.y = y;
        makeAndCacheConfigCommentWrappedToolTip(pWidth);
        Minecraft.getInstance().font.draw(pPoseStack, this.key, this.keyScreenPosition.x, this.keyScreenPosition.y, 16777215);
    }

    private void makeAndCacheConfigCommentWrappedToolTip(int pWidth) {
        String commentString = this.comment.getString();
        if (commentString.isEmpty()) {
            return;
        }
        if (cachedWidth != pWidth) {
            toolTip.clear();
            for (String lines : commentString.split("\n")) {
                StringBuilder comment = new StringBuilder();
                for (String word : lines.split("\s")) {
                    if (word.isBlank() && !lines.isBlank()) {
                        continue;
                    }
                    word = word.trim();
                    if (Minecraft.getInstance().font.width(word) > pWidth) {
                        char[] chars = word.toCharArray();
                        for (char aChar : chars) {
                            String character = Character.toString(aChar);
                            if (Minecraft.getInstance().font.width(comment.toString() + character) < pWidth) {
                                comment.append(character);
                            } else {
                                toolTip.add(new TextComponent(comment.toString()));
                                comment = new StringBuilder(character);
                            }
                        }
                        comment.append(" ");
                    } else {
                        if (Minecraft.getInstance().font.width(comment.toString() + word) < pWidth) {
                            comment.append(word).append(" ");
                        } else {
                            toolTip.add(new TextComponent(comment.toString()));
                            comment = new StringBuilder();
                            if (Minecraft.getInstance().font.width(word) > pWidth) {
                                char[] chars = word.toCharArray();
                                for (char aChar : chars) {
                                    String character = Character.toString(aChar);
                                    if (Minecraft.getInstance().font.width(comment.toString() + character) < pWidth) {
                                        comment.append(character);
                                    } else {
                                        toolTip.add(new TextComponent(comment.toString()));
                                        comment = new StringBuilder(character);
                                    }
                                }
                                comment.append(" ");
                            } else {
                                comment.append(word).append(" ");
                            }
                        }
                    }
                }
                if (!comment.isEmpty()) {
                    toolTip.add(new TextComponent(comment.toString()));
                }
            }
            cachedWidth = pWidth;
        }
    }

    @Nullable
    public T getValue() throws Exception {
        return null;
    }

    public void tick() {
    }

    public int getRowWidth() {
        return this.maxKeyWidth;
    }


}