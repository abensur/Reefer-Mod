#!/usr/bin/env python3
"""
Generate reefer item textures in different stages
Requires: pip install pillow
"""

from PIL import Image, ImageDraw

def create_fresh_reefer():
    """Fresh joint with twisted paper tip - horizontal with enhanced detail"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Enhanced color palette for better depth
    paper_base = (235, 233, 225, 255)
    paper_highlight = (248, 246, 240, 255)
    paper_shadow = (190, 185, 175, 255)
    paper_dark = (165, 160, 150, 255)

    # Whitish cardboard crutch/filter (like business card material)
    filter_base = (240, 240, 235, 255)
    filter_highlight = (250, 250, 248, 255)
    filter_shadow = (200, 200, 195, 255)

    twist_light = (225, 220, 210, 255)
    twist_shadow = (180, 175, 165, 255)

    # Twisted/pinched tip at left with more detail
    draw.point((1, 7), fill=twist_shadow)
    draw.point((1, 8), fill=twist_light)
    draw.point((1, 9), fill=twist_shadow)
    draw.line([(2, 7), (2, 9)], fill=twist_light)
    draw.point((2, 6), fill=paper_dark)
    draw.point((2, 10), fill=paper_dark)

    # Green cannabis visible at tip (minecraft:small_grass green)
    cannabis_green = (125, 170, 95, 255)
    cannabis_dark = (90, 130, 70, 255)
    draw.point((1, 8), fill=cannabis_dark)
    draw.point((2, 8), fill=cannabis_green)

    # Main paper body with subtle texture
    for x in range(3, 12):
        # Create subtle paper texture variation
        if x in [4, 7, 10]:
            draw.point((x, 7), fill=paper_highlight)
            draw.point((x, 8), fill=paper_base)
            draw.point((x, 9), fill=paper_base)
        else:
            draw.line([(x, 7), (x, 9)], fill=paper_base)

    # Top and bottom shading for cylindrical look
    for x in range(3, 12):
        draw.point((x, 6), fill=paper_shadow)
        draw.point((x, 10), fill=paper_shadow)

    # Add a seam line for realism (diagonal, where paper overlaps)
    draw.point((7, 7), fill=paper_shadow)
    draw.point((8, 8), fill=paper_shadow)
    draw.point((9, 9), fill=paper_shadow)

    # Filter at right with more detail
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.line([(13, 7), (13, 9)], fill=filter_base)
    draw.point((13, 8), fill=filter_highlight)  # Highlight
    draw.line([(14, 7), (14, 9)], fill=filter_shadow)
    draw.line([(12, 6), (14, 6)], fill=filter_shadow)
    draw.line([(12, 10), (14, 10)], fill=filter_shadow)

    return img

def create_fresh_lit():
    """Fresh joint actively being smoked with glowing ember"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Paper colors
    paper_base = (235, 233, 225, 255)
    paper_highlight = (248, 246, 240, 255)
    paper_shadow = (190, 185, 175, 255)
    paper_dark = (165, 160, 150, 255)

    # Filter
    filter_base = (240, 240, 235, 255)
    filter_highlight = (250, 250, 248, 255)
    filter_shadow = (200, 200, 195, 255)

    # Glowing ember colors
    ember_core = (255, 140, 0, 255)      # Bright orange core
    ember_bright = (255, 80, 20, 255)     # Bright red-orange
    ember_medium = (220, 50, 10, 255)     # Medium red
    ember_dark = (150, 30, 5, 255)        # Dark red edge

    # Burnt/charred
    burnt_dark = (40, 35, 30, 255)
    burnt_medium = (60, 55, 50, 255)

    # Glowing ember at tip (left side)
    draw.point((1, 8), fill=ember_core)  # Brightest center
    draw.point((2, 7), fill=ember_bright)
    draw.point((2, 8), fill=ember_bright)
    draw.point((2, 9), fill=ember_bright)
    draw.point((3, 7), fill=ember_medium)
    draw.point((3, 8), fill=ember_bright)
    draw.point((3, 9), fill=ember_medium)

    # Transition from ember to burnt
    draw.point((4, 7), fill=ember_dark)
    draw.point((4, 8), fill=ember_medium)
    draw.point((4, 9), fill=ember_dark)
    draw.point((4, 6), fill=burnt_dark)
    draw.point((4, 10), fill=burnt_dark)

    # Main paper body
    for x in range(5, 12):
        if x in [6, 9]:
            draw.point((x, 7), fill=paper_highlight)
            draw.point((x, 8), fill=paper_base)
            draw.point((x, 9), fill=paper_base)
        else:
            draw.line([(x, 7), (x, 9)], fill=paper_base)
        draw.point((x, 6), fill=paper_shadow)
        draw.point((x, 10), fill=paper_shadow)

    # Seam line (diagonal)
    draw.point((7, 7), fill=paper_shadow)
    draw.point((8, 8), fill=paper_shadow)
    draw.point((9, 9), fill=paper_shadow)

    # Filter at right
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.line([(13, 7), (13, 9)], fill=filter_base)
    draw.point((13, 8), fill=filter_highlight)
    draw.line([(14, 7), (14, 9)], fill=filter_shadow)
    draw.line([(12, 6), (14, 6)], fill=filter_shadow)
    draw.line([(12, 10), (14, 10)], fill=filter_shadow)

    return img

def create_half_lit():
    """Half-smoked joint actively burning with glowing ember"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Paper colors
    paper_base = (235, 233, 225, 255)
    paper_highlight = (248, 246, 240, 255)
    paper_shadow = (190, 185, 175, 255)

    # Filter
    filter_base = (240, 240, 235, 255)
    filter_highlight = (250, 250, 248, 255)
    filter_shadow = (200, 200, 195, 255)

    # Glowing ember
    ember_core = (255, 140, 0, 255)
    ember_bright = (255, 80, 20, 255)
    ember_medium = (220, 50, 10, 255)
    ember_dark = (150, 30, 5, 255)

    burnt_dark = (40, 35, 30, 255)

    # Glowing ember at burnt end (left side)
    draw.point((5, 8), fill=ember_core)
    draw.point((6, 7), fill=ember_bright)
    draw.point((6, 8), fill=ember_bright)
    draw.point((6, 9), fill=ember_bright)
    draw.point((7, 7), fill=ember_medium)
    draw.point((7, 8), fill=ember_bright)
    draw.point((7, 9), fill=ember_medium)
    draw.point((7, 6), fill=ember_dark)
    draw.point((7, 10), fill=ember_dark)

    # Transition zone
    draw.point((8, 7), fill=burnt_dark)
    draw.point((8, 8), fill=ember_dark)
    draw.point((8, 9), fill=burnt_dark)

    # Shorter paper body
    for x in range(9, 12):
        if x == 10:
            draw.point((x, 7), fill=paper_highlight)
            draw.point((x, 8), fill=paper_base)
            draw.point((x, 9), fill=paper_base)
        else:
            draw.line([(x, 7), (x, 9)], fill=paper_base)
        draw.point((x, 6), fill=paper_shadow)
        draw.point((x, 10), fill=paper_shadow)

    # Seam line (diagonal)
    draw.point((10, 7), fill=paper_shadow)
    draw.point((11, 8), fill=paper_shadow)

    # Filter at right
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.line([(13, 7), (13, 9)], fill=filter_base)
    draw.point((13, 8), fill=filter_highlight)
    draw.line([(14, 7), (14, 9)], fill=filter_shadow)
    draw.line([(12, 6), (14, 6)], fill=filter_shadow)
    draw.line([(12, 10), (14, 10)], fill=filter_shadow)

    return img

def create_roach_lit():
    """Tiny roach being smoked with small glowing ember"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Filter colors (stained)
    filter_base = (220, 215, 200, 255)
    filter_highlight = (235, 230, 220, 255)
    filter_shadow = (190, 185, 170, 255)
    filter_dark = (165, 160, 145, 255)

    # Stained paper
    paper_stained = (200, 190, 170, 255)
    paper_dark = (160, 150, 130, 255)

    # Small glowing ember
    ember_core = (255, 140, 0, 255)
    ember_bright = (255, 80, 20, 255)
    ember_medium = (220, 50, 10, 255)

    burnt_heavy = (40, 35, 25, 255)

    # Tiny glowing ember at burnt end
    draw.point((8, 8), fill=ember_core)
    draw.point((9, 7), fill=ember_bright)
    draw.point((9, 8), fill=ember_bright)
    draw.point((9, 9), fill=ember_bright)
    draw.point((9, 6), fill=ember_medium)
    draw.point((9, 10), fill=ember_medium)

    # Transition to paper
    draw.point((10, 7), fill=paper_dark)
    draw.point((10, 8), fill=paper_dark)
    draw.point((10, 9), fill=paper_dark)

    draw.point((11, 7), fill=paper_dark)
    draw.point((11, 8), fill=paper_stained)
    draw.point((11, 9), fill=paper_dark)

    # Filter dominates (right side)
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.point((12, 8), fill=filter_highlight)
    draw.line([(13, 7), (13, 9)], fill=filter_shadow)
    draw.point((13, 8), fill=filter_base)
    draw.line([(14, 7), (14, 9)], fill=filter_dark)
    draw.line([(12, 6), (14, 6)], fill=filter_dark)
    draw.line([(12, 10), (14, 10)], fill=filter_dark)

    return img

def create_smoked_reefer():
    """Smoked joint - burnt tip, slightly shorter than fresh"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Paper colors
    paper_base = (235, 233, 225, 255)
    paper_highlight = (248, 246, 240, 255)
    paper_shadow = (190, 185, 175, 255)
    paper_dark = (165, 160, 150, 255)

    # Filter
    filter_base = (240, 240, 235, 255)
    filter_highlight = (250, 250, 248, 255)
    filter_shadow = (200, 200, 195, 255)

    # Burnt colors
    burnt_dark = (40, 35, 30, 255)
    burnt_medium = (60, 55, 50, 255)
    ash_color = (160, 160, 160, 255)

    # Burnt end (extinguished)
    draw.point((2, 7), fill=burnt_dark)
    draw.point((2, 8), fill=burnt_medium)
    draw.point((2, 9), fill=burnt_dark)

    draw.point((3, 7), fill=burnt_medium)
    draw.point((3, 8), fill=ash_color)
    draw.point((3, 9), fill=burnt_medium)

    draw.point((4, 7), fill=burnt_dark)
    draw.point((4, 8), fill=burnt_medium)
    draw.point((4, 9), fill=burnt_dark)
    draw.point((4, 6), fill=burnt_dark)
    draw.point((4, 10), fill=burnt_dark)

    # Main paper body (slightly shorter)
    for x in range(5, 12):
        if x in [7, 10]:
            draw.point((x, 7), fill=paper_highlight)
            draw.point((x, 8), fill=paper_base)
            draw.point((x, 9), fill=paper_base)
        else:
            draw.line([(x, 7), (x, 9)], fill=paper_base)
        draw.point((x, 6), fill=paper_shadow)
        draw.point((x, 10), fill=paper_shadow)

    # Seam line
    draw.point((7, 7), fill=paper_shadow)
    draw.point((8, 8), fill=paper_shadow)
    draw.point((9, 9), fill=paper_shadow)

    # Filter at right
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.line([(13, 7), (13, 9)], fill=filter_base)
    draw.point((13, 8), fill=filter_highlight)
    draw.line([(14, 7), (14, 9)], fill=filter_shadow)
    draw.line([(12, 6), (14, 6)], fill=filter_shadow)
    draw.line([(12, 10), (14, 10)], fill=filter_shadow)

    return img

def create_smoked_burning():
    """Smoked joint actively burning with glowing ember"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Paper colors
    paper_base = (235, 233, 225, 255)
    paper_highlight = (248, 246, 240, 255)
    paper_shadow = (190, 185, 175, 255)

    # Filter
    filter_base = (240, 240, 235, 255)
    filter_highlight = (250, 250, 248, 255)
    filter_shadow = (200, 200, 195, 255)

    # Glowing ember
    ember_core = (255, 140, 0, 255)
    ember_bright = (255, 80, 20, 255)
    ember_medium = (220, 50, 10, 255)
    ember_dark = (150, 30, 5, 255)

    burnt_dark = (40, 35, 30, 255)

    # Glowing ember at burnt end
    draw.point((2, 8), fill=ember_core)
    draw.point((3, 7), fill=ember_bright)
    draw.point((3, 8), fill=ember_bright)
    draw.point((3, 9), fill=ember_bright)
    draw.point((4, 7), fill=ember_medium)
    draw.point((4, 8), fill=ember_bright)
    draw.point((4, 9), fill=ember_medium)
    draw.point((4, 6), fill=ember_dark)
    draw.point((4, 10), fill=ember_dark)

    # Transition zone
    draw.point((5, 7), fill=burnt_dark)
    draw.point((5, 8), fill=ember_dark)
    draw.point((5, 9), fill=burnt_dark)

    # Main paper body
    for x in range(6, 12):
        if x in [8, 10]:
            draw.point((x, 7), fill=paper_highlight)
            draw.point((x, 8), fill=paper_base)
            draw.point((x, 9), fill=paper_base)
        else:
            draw.line([(x, 7), (x, 9)], fill=paper_base)
        draw.point((x, 6), fill=paper_shadow)
        draw.point((x, 10), fill=paper_shadow)

    # Seam line (diagonal)
    draw.point((8, 7), fill=paper_shadow)
    draw.point((9, 8), fill=paper_shadow)
    draw.point((10, 9), fill=paper_shadow)

    # Filter at right
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.line([(13, 7), (13, 9)], fill=filter_base)
    draw.point((13, 8), fill=filter_highlight)
    draw.line([(14, 7), (14, 9)], fill=filter_shadow)
    draw.line([(12, 6), (14, 6)], fill=filter_shadow)
    draw.line([(12, 10), (14, 10)], fill=filter_shadow)

    return img

def create_half_reefer():
    """Half-smoked joint, shorter - horizontal with enhanced detail"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Enhanced colors
    paper_base = (235, 233, 225, 255)
    paper_highlight = (248, 246, 240, 255)
    paper_shadow = (190, 185, 175, 255)

    # Whitish cardboard crutch/filter
    filter_base = (240, 240, 235, 255)
    filter_highlight = (250, 250, 248, 255)
    filter_shadow = (200, 200, 195, 255)

    burnt_dark = (30, 25, 20, 255)
    burnt_medium = (50, 45, 40, 255)
    ash_color = (160, 160, 160, 255)

    ember_bright = (255, 100, 10, 255)
    ember_medium = (220, 70, 0, 255)
    ember_dark = (150, 40, 0, 255)

    # Extinguished burnt end (no active ember) at left
    draw.point((5, 7), fill=burnt_dark)
    draw.point((5, 8), fill=burnt_medium)
    draw.point((5, 9), fill=burnt_dark)

    draw.point((6, 7), fill=burnt_medium)
    draw.point((6, 8), fill=ash_color)
    draw.point((6, 9), fill=burnt_medium)

    # Burnt section with texture
    draw.point((7, 7), fill=burnt_dark)
    draw.point((7, 8), fill=burnt_medium)
    draw.point((7, 9), fill=burnt_dark)
    draw.point((7, 6), fill=burnt_dark)
    draw.point((7, 10), fill=burnt_dark)

    # Shorter paper body with detail
    for x in range(8, 12):
        if x == 10:
            draw.point((x, 7), fill=paper_highlight)
            draw.point((x, 8), fill=paper_base)
            draw.point((x, 9), fill=paper_base)
        else:
            draw.line([(x, 7), (x, 9)], fill=paper_base)
        draw.point((x, 6), fill=paper_shadow)
        draw.point((x, 10), fill=paper_shadow)

    # Seam line (diagonal)
    draw.point((9, 7), fill=paper_shadow)
    draw.point((10, 8), fill=paper_shadow)
    draw.point((11, 9), fill=paper_shadow)

    # Filter at right
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.line([(13, 7), (13, 9)], fill=filter_base)
    draw.point((13, 8), fill=filter_highlight)
    draw.line([(14, 7), (14, 9)], fill=filter_shadow)
    draw.line([(12, 6), (14, 6)], fill=filter_shadow)
    draw.line([(12, 10), (14, 10)], fill=filter_shadow)

    return img

def create_roach():
    """Tiny roach - just a small stub - horizontal with enhanced detail"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Enhanced colors for roach - slightly yellowed/stained white crutch
    filter_base = (220, 215, 200, 255)
    filter_highlight = (235, 230, 220, 255)
    filter_shadow = (190, 185, 170, 255)
    filter_dark = (165, 160, 145, 255)

    # Heavily stained/resin-covered paper
    paper_stained = (200, 190, 170, 255)
    paper_dark = (160, 150, 130, 255)

    # Very burnt end
    burnt_heavy = (40, 35, 25, 255)
    burnt_brown = (70, 60, 45, 255)
    ash_dark = (100, 95, 85, 255)

    # Dark burnt spot at left end
    draw.point([(8, 7), (8, 8), (8, 9)], fill=(30, 30, 30, 255))

    draw.point((8, 8), fill=burnt_heavy)  # Final burnt nub

    # Heavily burnt/charred end
    draw.point((9, 7), fill=burnt_brown)
    draw.point((9, 8), fill=ash_dark)
    draw.point((9, 9), fill=burnt_brown)
    draw.point((9, 6), fill=burnt_heavy)
    draw.point((9, 10), fill=burnt_heavy)

    # Tiny remaining paper (stained and discolored)
    draw.point((10, 7), fill=paper_dark)
    draw.point((10, 8), fill=paper_dark)
    draw.point((10, 9), fill=paper_dark)

    draw.point((11, 7), fill=paper_dark)
    draw.point((11, 8), fill=paper_stained)
    draw.point((11, 9), fill=paper_dark)

    # Filter dominates now (right side) - well-used look
    draw.line([(12, 7), (12, 9)], fill=filter_base)
    draw.point((12, 8), fill=filter_highlight)
    draw.line([(13, 7), (13, 9)], fill=filter_shadow)
    draw.point((13, 8), fill=filter_base)
    draw.line([(14, 7), (14, 9)], fill=filter_dark)
    draw.line([(12, 6), (14, 6)], fill=filter_dark)
    draw.line([(12, 10), (14, 10)], fill=filter_dark)

    return img

def create_rolling_paper():
    """Create rolling paper with gum strip and center crease - matching reefer art style"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Color palette matching reefer paper style
    paper_base = (245, 243, 238, 255)
    paper_highlight = (252, 250, 245, 255)
    paper_shadow = (200, 195, 185, 255)
    paper_dark = (175, 170, 160, 255)
    paper_edge = (155, 150, 140, 255)

    # Gum strip colors (slightly amber/brownish tint)
    gum_base = (225, 210, 190, 255)
    gum_highlight = (235, 220, 200, 255)
    gum_shadow = (200, 185, 165, 255)

    # Left edge with shadow
    draw.line([(1, 6), (1, 10)], fill=paper_dark)
    draw.point((1, 5), fill=paper_edge)
    draw.point((1, 11), fill=paper_edge)

    # Main paper body
    for x in range(2, 14):
        # Gum strip at top edge (distinctive feature of rolling papers)
        draw.point((x, 5), fill=gum_shadow)
        draw.point((x, 6), fill=gum_base)
        if x in [4, 7, 10]:
            draw.point((x, 6), fill=gum_highlight)

        # Main paper body with subtle texture
        for y in range(7, 10):
            if x in [4, 7, 10, 13]:
                draw.point((x, y), fill=paper_highlight)
            else:
                draw.point((x, y), fill=paper_base)

        # Bottom edge
        draw.point((x, 10), fill=paper_shadow)
        draw.point((x, 11), fill=paper_edge)

    # Center fold/crease line (horizontal line across the middle)
    fold_color = (210, 205, 195, 255)
    for x in range(2, 14):
        draw.point((x, 8), fill=fold_color)

    # Right edge with shadow (stops at x=14)
    draw.line([(14, 6), (14, 10)], fill=paper_dark)
    draw.point((14, 5), fill=gum_shadow)
    draw.point((14, 11), fill=paper_edge)

    # Corner darkening for depth
    draw.point((2, 6), fill=paper_dark)
    draw.point((2, 10), fill=paper_dark)
    draw.point((13, 6), fill=paper_dark)
    draw.point((13, 10), fill=paper_dark)

    return img

def main():
    import os

    # Get the textures directory
    base_path = "src/main/resources/assets/reefer/textures/item"

    if not os.path.exists(base_path):
        print(f"Creating directory: {base_path}")
        os.makedirs(base_path, exist_ok=True)

    # Generate all textures
    textures = {
        "reefer_fresh.png": create_fresh_reefer(),
        "reefer_fresh_burning.png": create_fresh_lit(),
        "reefer_smoked.png": create_smoked_reefer(),
        "reefer_smoked_burning.png": create_smoked_burning(),
        "reefer_half.png": create_half_reefer(),
        "reefer_half_burning.png": create_half_lit(),
        "reefer_roach.png": create_roach(),
        "reefer_roach_burning.png": create_roach_lit(),
        "rolling_paper.png": create_rolling_paper()
    }

    for filename, img in textures.items():
        filepath = os.path.join(base_path, filename)
        img.save(filepath)
        print(f"✓ Created {filename}")

    print("\nAll textures generated successfully!")
    print("You can now test them in-game or edit them with an image editor.")

if __name__ == "__main__":
    main()
