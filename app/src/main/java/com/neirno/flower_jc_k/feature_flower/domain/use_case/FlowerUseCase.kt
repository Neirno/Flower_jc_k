package com.neirno.flower_jc_k.feature_flower.domain.use_case

data class FlowerUseCases(
    val getFlowers: GetFlowers,
    val deleteFlower: DeleteFlower,
    val addFlower: AddFlower,
    val getFlower: GetFlower,
    val updateFlower: UpdateFlower,
    val updateFertilizingDate: UpdateFertilizingDate,
    val updateSprayingDate: UpdateSprayingDate,
    val updateWateringDate: UpdateWateringDate,
    val setAlarmForFlower: SetAlarmForFlower,
    val cancelAlarmForFlower: CancelAlarmForFlower,
    val checkAlarmForFlower: CheckAlarmForFlower,
    val saveImage: SaveImage,
    val deleteImage: DeleteImage
)