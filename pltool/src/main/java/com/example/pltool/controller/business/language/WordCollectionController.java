package com.example.pltool.controller.business.language;

import com.example.pltool.domain.dto.flashcard.cardpackage.PackageCollectionData;
import com.example.pltool.domain.dto.flashcard.cardpackage.RemoveCollectionOfPackage;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.service.language.WordCollectionService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wordCollection")
public class WordCollectionController extends BaseController {
    @Autowired
    private WordCollectionService wordCollectionService;

    @GetMapping("/list")
    TableDataInfo list(@RequestParam(value = "name", required = false) String name) {
        startPage();
        return getDataTable(wordCollectionService.getAllWordCollection());
    }

    @GetMapping("/getAllWordCollection")
    AjaxResult getAllWordCollection() {
        return AjaxResult.success(wordCollectionService.getAllWordCollection());
    }

    @PostMapping("/updateWordCollectionData")
    AjaxResult updateWordCollectionData(@RequestBody WordCollectionData wordCollectionData) {
        return wordCollectionService.updateWordCollectionData(wordCollectionData);
    }

    @GetMapping("/getInfo")
    AjaxResult getInfo(@RequestParam("uuid") String uuid) {
        return AjaxResult.success(wordCollectionService.getInfo(uuid));
    }

    @GetMapping("/getWordsOfCollection")
    TableDataInfo getWordsOfCollection(@RequestParam("labelUUID") String labelUUID) {
        startPage();
        return getDataTable(wordCollectionService.getWordsOfCollection(labelUUID));
    }

    @GetMapping("/getCollectionsOfPackage")
    TableDataInfo getCollectionsOfPackage(@RequestParam("packageUUId") String packageUUId) {
        startPage();
        return getDataTable(wordCollectionService.getCollectionsOfPackage(packageUUId, getUserId()));
    }

    /**
     * 查询卡片关联的卡包信息
     */
    @GetMapping("/getCollectionOfWord")
    public TableDataInfo getCollectionOfWord(@RequestParam("wordUUId") String wordUUId) {
        startPage();
        return getDataTable(wordCollectionService.getCollectionOfWord(wordUUId, getUserId()));
    }

    @PostMapping("/addWordToCollection")
    AjaxResult addWordToCollection(@RequestBody WordCollectionData wordCollectionData) {
        wordCollectionData.setUserId(getUserId());
        return wordCollectionService.addWordToCollection(wordCollectionData);
    }

    @GetMapping("/getAllLabels")
    AjaxResult getAllLabels() {
        return AjaxResult.success(wordCollectionService.getAllLabels(getUserId()));
    }

    @PostMapping("/addCollectionToPackage")
    AjaxResult addCollectionToPackage(@RequestBody PackageCollectionData packageCollectionData) {
        packageCollectionData.setUserId(getUserId());
        return wordCollectionService.addCollectionToPackage(packageCollectionData);
    }

    @PostMapping("/removeWordOfCollection")
    AjaxResult removeWordOfCollection(@RequestBody WordCollectionData wordCollectionData) {
        wordCollectionData.setUserId(getUserId());
        return wordCollectionService.removeWordOfCollection(wordCollectionData);
    }

    @PostMapping("/removeCollectionOfPackage")
    AjaxResult removeCollectionOfPackage(@RequestBody RemoveCollectionOfPackage removeCollectionOfPackage) {
        removeCollectionOfPackage.setUserId(getUserId());
        return wordCollectionService.removeCollectionOfPackage(removeCollectionOfPackage);
    }
}
