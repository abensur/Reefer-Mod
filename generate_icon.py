#!/usr/bin/env python3
"""
Generate a 512x512 mod icon for the Reefer mod
Requires: pip install pillow
"""

from PIL import Image, ImageDraw

def create_mod_icon():
    """Create a 512x512 mod icon featuring a reefer"""
    # Create high-res canvas
    size = 512
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Scale factor (16px -> 512px = 32x)
    scale = 32

    # Background - soft gradient circle
    for i in range(256, 0, -4):
        radius = i
        alpha = int(80 * (radius / 256))
        color = (40, 40, 45, alpha)
        draw.ellipse(
            [(256 - radius, 256 - radius), (256 + radius, 256 + radius)],
            fill=color
        )

    # Enhanced color palette (scaled up)
    paper_base = (235, 233, 225, 255)
    paper_highlight = (248, 246, 240, 255)
    paper_shadow = (190, 185, 175, 255)
    paper_dark = (165, 160, 150, 255)
    filter_base = (240, 240, 235, 255)
    filter_highlight = (250, 250, 248, 255)
    cannabis_green = (125, 170, 95, 255)
    cannabis_dark = (90, 130, 70, 255)
    ember_bright = (255, 180, 60, 255)
    ember_core = (255, 220, 120, 255)
    smoke = (200, 200, 210, 180)

    # Center and rotate the reefer slightly for dynamic look
    center_x = 256
    center_y = 256

    # Draw reefer body (horizontal, centered, larger)
    reefer_length = 320
    reefer_width = 48
    start_x = center_x - reefer_length // 2
    start_y = center_y - reefer_width // 2

    # Twisted tip (left side)
    twist_start = start_x
    for i in range(3):
        y_offset = start_y + reefer_width // 2 - 16 + i * 16
        draw.ellipse(
            [(twist_start - 8 + i * 8, y_offset), (twist_start + 8 + i * 8, y_offset + 16)],
            fill=paper_shadow if i % 2 == 0 else paper_highlight
        )

    # Green cannabis at tip
    draw.ellipse(
        [(twist_start + 8, start_y + 12), (twist_start + 24, start_y + 36)],
        fill=cannabis_green
    )
    draw.ellipse(
        [(twist_start + 12, start_y + 16), (twist_start + 20, start_y + 32)],
        fill=cannabis_dark
    )

    # Main paper body with gradient
    for x in range(start_x + 40, start_x + reefer_length - 80, 4):
        # Create subtle texture
        brightness = 1.0 - abs(x - center_x) / (reefer_length / 2) * 0.15
        color = tuple(int(c * brightness) if i < 3 else c for i, c in enumerate(paper_base))
        draw.rectangle(
            [(x, start_y), (x + 4, start_y + reefer_width)],
            fill=color
        )

    # Add paper seam line
    seam_y = start_y + reefer_width // 3
    draw.line(
        [(start_x + 40, seam_y), (start_x + reefer_length - 80, seam_y)],
        fill=paper_shadow,
        width=2
    )

    # Filter/crutch (right side)
    filter_start = start_x + reefer_length - 80
    filter_width = 64

    # Filter with circular pattern
    draw.rectangle(
        [(filter_start, start_y), (filter_start + filter_width, start_y + reefer_width)],
        fill=filter_base
    )
    draw.rectangle(
        [(filter_start, start_y + 4), (filter_start + filter_width, start_y + reefer_width - 4)],
        fill=filter_highlight
    )

    # Circular pattern on filter
    for i in range(3):
        circle_x = filter_start + 16 + i * 16
        circle_y = start_y + reefer_width // 2
        draw.ellipse(
            [(circle_x - 6, circle_y - 6), (circle_x + 6, circle_y + 6)],
            outline=paper_shadow,
            width=2
        )

    # Lit end with ember (right edge)
    ember_x = start_x + reefer_length - 16
    ember_y = start_y + reefer_width // 2

    # Outer glow
    for i in range(5, 0, -1):
        alpha = int(100 * (i / 5))
        draw.ellipse(
            [(ember_x - i * 8, ember_y - i * 8), (ember_x + i * 8, ember_y + i * 8)],
            fill=(255, 150, 50, alpha)
        )

    # Bright ember core
    draw.ellipse(
        [(ember_x - 12, ember_y - 12), (ember_x + 12, ember_y + 12)],
        fill=ember_bright
    )
    draw.ellipse(
        [(ember_x - 6, ember_y - 6), (ember_x + 6, ember_y + 6)],
        fill=ember_core
    )

    # Smoke trail
    smoke_start_x = ember_x + 16
    for i in range(6):
        smoke_x = smoke_start_x + i * 24
        smoke_y = ember_y - i * 20 - 10
        smoke_size = 16 + i * 8
        alpha = int(smoke[3] * (1 - i / 6))
        draw.ellipse(
            [(smoke_x - smoke_size, smoke_y - smoke_size),
             (smoke_x + smoke_size, smoke_y + smoke_size)],
            fill=(smoke[0], smoke[1], smoke[2], alpha)
        )

    # Add subtle shadow below reefer
    shadow_y = start_y + reefer_width + 8
    for i in range(3):
        alpha = int(40 * (3 - i) / 3)
        draw.ellipse(
            [(start_x + 40 - i * 8, shadow_y + i * 4),
             (start_x + reefer_length - 80 + i * 8, shadow_y + 8 + i * 4)],
            fill=(0, 0, 0, alpha)
        )

    return img

if __name__ == "__main__":
    icon = create_mod_icon()
    icon.save("src/main/resources/reefer_logo.png")
    print("✓ Generated reefer_logo.png (512x512)")
