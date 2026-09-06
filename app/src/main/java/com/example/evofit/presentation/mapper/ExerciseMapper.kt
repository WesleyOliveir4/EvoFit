package com.example.evofit.presentation.mapper

import com.example.evofit.R

object ExerciseMapper {
    /**
     * Mapeia o ID do exercício para um recurso de imagem.
     * Cada exercício tem sua própria entrada para facilitar a personalização individual das imagens.
     */
    fun toImageRes(exerciseId: String): Int {
        return when (exerciseId) {
            // Back (1-10, 92-94)
            "1" -> R.drawable.img_puxada_alta
            "2" -> R.drawable.img_barra_fixa
            "3" -> R.drawable.img_remada_curvada_2
            "4" -> R.drawable.img_remada_unilateral_1
            "5" -> R.drawable.img_remada_baixa
            "6" -> R.drawable.img_pull_down
            "7" -> R.drawable.img_remada_cavalinho
            "8" -> R.drawable.img_pull_down
            "9" -> R.drawable.img_remada_t_bar
            "10" -> R.drawable.img_levantamento_terra
            "92" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "93" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "94" -> R.drawable.img_cardio // Necessário criar imagem para este treino

            // Chest (11-20, 95-96)
            "11" -> R.drawable.img_supino_reto
            "12" -> R.drawable.img_supino_inclinado
            "13" -> R.drawable.img_supino_declinado
            "14" -> R.drawable.img_crucifixo
            "15" -> R.drawable.img_crucifixo_inclinado
            "16" -> R.drawable.img_peck_deck
            "17" -> R.drawable.img_crossover
            "18" -> R.drawable.img_flexao_de_braco
            "19" -> R.drawable.img_chest_press
            "20" -> R.drawable.img_chest_press
            "95" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "96" -> R.drawable.img_cardio // Necessário criar imagem para este treino

            // Legs (21-31, 97-100)
            "21" -> R.drawable.img_agachamento_livre
            "22" -> R.drawable.img_leg_press
            "23" -> R.drawable.img_hack_squat
            "24" -> R.drawable.img_cadeira_extensora
            "25" -> R.drawable.img_mesa_flexora
            "26" -> R.drawable.img_cadeira_flexora
            "27" -> R.drawable.img_afundo
            "28" -> R.drawable.img_passada
            "29" -> R.drawable.img_agachamento_bulgaro
            "30" -> R.drawable.img_stiff
            "31" -> R.drawable.img_stiff
            "97" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "98" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "99" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "100" -> R.drawable.img_cardio // Necessário criar imagem para este treino

            // Biceps (32-38, 101)
            "32" -> R.drawable.img_rosca_direta
            "33" -> R.drawable.img_rosca_direta_pulley
            "34" -> R.drawable.img_rosca_alternada
            "35" -> R.drawable.img_rosca_martelo
            "36" -> R.drawable.img_rosca_martelo_pulley
            "37" -> R.drawable.img_rosca_scott
            "38" -> R.drawable.img_rosca_concentrada
            "101" -> R.drawable.img_cardio // Necessário criar imagem para este treino

            // Triceps (39-43, 102-104)
            "39" -> R.drawable.img_triceps_pulley
            "40" -> R.drawable.img_triceps_pulley_corda
            "41" -> R.drawable.img_triceps_frances
            "42" -> R.drawable.img_triceps_testa
            "43" -> R.drawable.img_paralela_2
            "102" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "103" -> R.drawable.img_cardio // Necessário criar imagem para este treino
            "104" -> R.drawable.img_cardio // Necessário criar imagem para este treino

            // Forearms (44-45)
            "44" -> R.drawable.img_rosca_inversa
            "45" -> R.drawable.img_flexao_de_punho

            // Shoulders (46-54)
            "46" -> R.drawable.img_desenvolvimento_militar
            "47" -> R.drawable.img_desenvolvimento_halter
            "48" -> R.drawable.img_elevacao_lateral
            "49" -> R.drawable.img_elevacao_frontal
            "50" -> R.drawable.img_crucifixo_inverso
            "51" -> R.drawable.img_face_pull
            "52" -> R.drawable.img_desenvolvimento_maquina
            "53" -> R.drawable.img_remada_alta
            "54" -> R.drawable.img_arnold_press

            // Core (55-64)
            "55" -> R.drawable.img_abdominal_tradicional
            "56" -> R.drawable.img_abdominal_infra
            "57" -> R.drawable.img_prancha
            "58" -> R.drawable.img_prancha_lateral
            "59" -> R.drawable.img_elevacao_pernas
            "60" -> R.drawable.img_abdominal_bicicleta
            "61" -> R.drawable.img_abdominal_maquina
            "62" -> R.drawable.img_crunch_polia
            "63" -> R.drawable.img_russian_twist
            "64" -> R.drawable.img_mountain_climber

            // Cardio (65-74)
            "65" -> R.drawable.img_esteira_caminhada
            "66" -> R.drawable.img_esteira_corrida
            "67" -> R.drawable.img_bicicleta_ergometrica
            "68" -> R.drawable.img_eliptico
            "69" -> R.drawable.img_escada
            "70" -> R.drawable.img_remo
            "71" -> R.drawable.img_pular_corda
            "72" -> R.drawable.img_corrida_externa
            "73" -> R.drawable.img_esteira_caminhada
            "74" -> R.drawable.img_hiit

            // Glutes (75-84)
            "75" -> R.drawable.img_elevacao_pelvica
            "76" -> R.drawable.img_elevacao_pelvica
            "77" -> R.drawable.img_coice_polia
            "78" -> R.drawable.img_abducao_quadril
            "79" -> R.drawable.img_agachamento_sumo
            "80" -> R.drawable.img_stiff
            "81" -> R.drawable.img_afundo
            "82" -> R.drawable.img_passada
            "83" -> R.drawable.img_gluteo_maquina
            "84" -> R.drawable.img_ponte_gluteo

            // Calves (85-91)
            "85" -> R.drawable.img_panturrilha_em_pe
            "86" -> R.drawable.img_panturrilha_sentado
            "87" -> R.drawable.img_panturrilha_leg_press
            "88" -> R.drawable.img_panturrilha_smith
            "89" -> R.drawable.img_panturrilha_unilateral
            "90" -> R.drawable.img_panturrilha_hack
            "91" -> R.drawable.img_panturrilha_degrau

            else -> R.drawable.img_cardio
        }
    }
}
