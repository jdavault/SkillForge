package com.example.skillforge.data.starter

import com.example.skillforge.data.model.LearningLevel

object DrummerStarterContent {

    val skillName = "Drumming Fundamentals"
    val skillDescription = "Master essential drumming techniques, from basic rudiments to creative expression."
    val skillIcon = "asset:///drumming/drum_icon.png"

    data class FlashcardData(
        val front: String,
        val back: String,
        val level: LearningLevel,
        val imageUri: String? = null,
        val audioUri: String? = null
    )

    val flashcards: List<FlashcardData> = listOf(
        // ── REMEMBER (Level 1) – Recall facts ──
        FlashcardData(
            front = "What is a paradiddle?",
            back = "A paradiddle is a drum rudiment with the sticking pattern: RLRR LRLL",
            level = LearningLevel.REMEMBER
        ),
        FlashcardData(
            front = "What is a flam?",
            back = "A flam is a grace note played just before the main stroke, creating a thicker sound.",
            level = LearningLevel.REMEMBER
        ),
        FlashcardData(
            front = "What is a drag?",
            back = "A drag consists of two grace notes (a buzz) followed by a main stroke.",
            level = LearningLevel.REMEMBER
        ),
        FlashcardData(
            front = "How many beats does a whole note get in 4/4 time?",
            back = "A whole note gets 4 beats in 4/4 time.",
            level = LearningLevel.REMEMBER
        ),
        FlashcardData(
            front = "What does 'BPM' stand for?",
            back = "BPM stands for Beats Per Minute — the standard measurement of tempo.",
            level = LearningLevel.REMEMBER
        ),

        // ── UNDERSTAND (Level 2) – Explain concepts ──
        FlashcardData(
            front = "Explain why matched grip and traditional grip produce different sounds.",
            back = "Matched grip provides equal power from both hands. Traditional grip angles the left stick differently, producing a lighter tone ideal for jazz dynamics.",
            level = LearningLevel.UNDERSTAND
        ),
        FlashcardData(
            front = "Why are rudiments considered the foundation of drumming?",
            back = "Rudiments are standardized sticking patterns that build hand coordination, speed, and control. Complex patterns are combinations of rudiments.",
            level = LearningLevel.UNDERSTAND
        ),
        FlashcardData(
            front = "Explain the difference between simple and compound time signatures.",
            back = "Simple time (e.g., 4/4) divides beats into two equal parts. Compound time (e.g., 6/8) divides beats into three, creating a swing or shuffle feel.",
            level = LearningLevel.UNDERSTAND
        ),
        FlashcardData(
            front = "What is the role of ghost notes in a groove?",
            back = "Ghost notes are very soft snare hits between main beats that add texture and feel to a groove without changing the core rhythm.",
            level = LearningLevel.UNDERSTAND
        ),

        // ── APPLY (Level 3) – Use in new situations ──
        FlashcardData(
            front = "Play a basic rock beat at 80 BPM for one minute without stopping.",
            back = "Basic rock beat: Hi-hat on 8ths, snare on 2 & 4, kick on 1 & 3. Focus on steady tempo and even hi-hat volume.",
            level = LearningLevel.APPLY
        ),
        FlashcardData(
            front = "Apply a paradiddle pattern to the drum kit by moving the right hand between hi-hat and ride.",
            back = "RLRR on hi-hat/snare, then LRLL moving R to ride cymbal. This creates melodic variation while maintaining the rudiment.",
            level = LearningLevel.APPLY
        ),
        FlashcardData(
            front = "Play a shuffle groove at 100 BPM.",
            back = "Shuffle feel: swing the 8th notes (long-short pattern), snare on 2 & 4, kick on 1 & 3. The triplet feel is key.",
            level = LearningLevel.APPLY
        ),
        FlashcardData(
            front = "Demonstrate dynamic control: play 8 bars, crescendo from pp to ff.",
            back = "Start with stick height ~1 inch (pp), gradually raise to ~12 inches (ff) over 8 bars. Keep tempo constant while increasing volume.",
            level = LearningLevel.APPLY
        ),
        FlashcardData(
            front = "Play a four-bar phrase using flams on beats 2 and 4.",
            back = "Replace regular snare hits with flams on beats 2 and 4. The grace note should be noticeably softer than the primary stroke.",
            level = LearningLevel.APPLY
        ),

        // ── ANALYZE (Level 4) – Draw connections ──
        FlashcardData(
            front = "Compare a standard rock beat with a half-time feel. What changes and why?",
            back = "Half-time moves the snare from beats 2 & 4 to beat 3 only, doubling the perceived bar length. This creates a heavier, slower feel without changing tempo.",
            level = LearningLevel.ANALYZE
        ),
        FlashcardData(
            front = "Listen to a funk groove and identify which ghost notes are essential to the feel.",
            back = "Essential ghost notes typically fall on the 'e' and 'a' of beats (16th note subdivisions). Removing them flattens the groove. The ones closest to backbeats (2 & 4) are most critical.",
            level = LearningLevel.ANALYZE
        ),
        FlashcardData(
            front = "Analyze why a 12/8 blues shuffle feels different from a straight 4/4 rock beat at the same tempo.",
            back = "12/8 groups pulses in threes (compound meter), creating a lilt/swing. 4/4 rock uses straight 8ths (simple meter). The subdivision changes the underlying feel entirely.",
            level = LearningLevel.ANALYZE
        ),
        FlashcardData(
            front = "Break down a linear drum fill and explain why no two limbs hit simultaneously.",
            back = "Linear patterns create clarity by separating each voice. This produces a melodic, flowing fill where each drum speaks individually rather than being masked by simultaneous hits.",
            level = LearningLevel.ANALYZE
        ),

        // ── EVALUATE (Level 5) – Justify decisions ──
        FlashcardData(
            front = "A band asks you to choose between a busy fill and a simple crash into the chorus. Justify your choice.",
            back = "A simple crash usually serves the music better — it marks the section change clearly and lets the band's energy carry the transition. Busy fills can distract from the song's momentum.",
            level = LearningLevel.EVALUATE
        ),
        FlashcardData(
            front = "Evaluate whether a click track helps or hurts a live performance.",
            back = "Click tracks ensure consistency and sync with backing tracks, but can reduce natural dynamics and feel. Best used when syncing is required; optional for purely acoustic performances where push/pull enhances emotion.",
            level = LearningLevel.EVALUATE
        ),
        FlashcardData(
            front = "Critique a drum solo that uses only speed and no dynamics. What's missing?",
            back = "Speed alone lacks musicality. Effective solos use contrast: loud/soft, fast/slow, dense/sparse. Dynamics create tension and release, making the solo a journey rather than an endurance test.",
            level = LearningLevel.EVALUATE
        ),

        // ── CREATE (Level 6) – Produce original work ──
        FlashcardData(
            front = "Compose a 4-bar drum intro for a song that starts quiet and builds.",
            back = "Example: Bar 1 — cross-stick quarter notes. Bar 2 — add hi-hat 8ths. Bar 3 — open hi-hat, add kick. Bar 4 — full groove with crash on beat 1 of the next section.",
            level = LearningLevel.CREATE
        ),
        FlashcardData(
            front = "Create a unique groove by combining elements from two different genres (e.g., jazz + funk).",
            back = "Example: Use a jazz ride pattern (swing 8ths on ride) with a funk kick/snare pattern (syncopated 16ths on kick, ghost notes on snare). The contrast creates a fresh hybrid feel.",
            level = LearningLevel.CREATE
        ),
        FlashcardData(
            front = "Design a 2-minute practice routine that targets your weakest rudiment.",
            back = "Structure: 30s slow singles (warm-up) → 30s the weak rudiment at 60% speed → 30s at 80% speed → 30s alternating the weak rudiment with a strong one. Log BPM achieved.",
            level = LearningLevel.CREATE
        ),
        FlashcardData(
            front = "Write a drum chart for an original 8-bar verse section.",
            back = "Use standard notation or shorthand. Include: time signature, tempo, kick pattern, snare placement, hi-hat/ride choice, any fills, and dynamic markings. The chart should be readable by another drummer.",
            level = LearningLevel.CREATE
        ),
    )
}
