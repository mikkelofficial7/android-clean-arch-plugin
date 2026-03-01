package com.android.kotlin.architecture

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import javax.swing.*

class Main : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val dialog = GenerateSolidArchDialog(project)

        if (dialog.showAndGet()) {
            val activity = dialog.getActivityName()
            val viewModel = dialog.getViewModelName()
            val useCase = dialog.getUseCaseName()
            val repository = dialog.getRepositoryName()

            if (project != null && !useCase.isNullOrEmpty() && !repository.isNullOrEmpty() && !viewModel.isNullOrEmpty() && !activity.isNullOrEmpty()) {
                FileGenerator(project, activity.toPascalCase(), viewModel.toPascalCase(), useCase.toPascalCase(), repository.toPascalCase()).execute()
                SuccessAlertDialog().show()
            }
        }
    }
}

class GenerateSolidArchDialog(project: Project?) : DialogWrapper(project) {
    private val activityField = JTextField()
    private val viewModelField = JTextField()
    private val useCaseField = JTextField()
    private val repositoryField = JTextField()

    init {
        title = "Generate Minimum Kotlin S.O.L.I.D Architecture"
        init()
    }

    override fun doValidate(): ValidationInfo? {
        if (activityField.text.isBlank()) {
            return ValidationInfo(
                "Activity name cannot be empty",
                activityField
            )
        }

        if (viewModelField.text.isBlank()) {
            return ValidationInfo(
                "ViewModel name cannot be empty",
                viewModelField
            )
        }

        if (useCaseField.text.isBlank()) {
            return ValidationInfo(
                "UseCase name cannot be empty",
                useCaseField
            )
        }

        if (repositoryField.text.isBlank()) {
            return ValidationInfo(
                "Repository name cannot be empty",
                repositoryField
            )
        }

        return null
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.minimumSize = Dimension(400, 200)
        panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        // helper to make fields stretch horizontally
        fun stretch(field: JTextField) {
            field.alignmentX = JComponent.LEFT_ALIGNMENT
            field.maximumSize = Dimension(Int.MAX_VALUE, field.preferredSize.height)
        }

        // Activity
        val labelActivity = JLabel("Insert Targeted Activity FileName:")
        activityField.text = "Main Activity"
        labelActivity.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(labelActivity)
        panel.add(Box.createVerticalStrut(4))
        stretch(activityField)
        panel.add(activityField)

        // ViewModel
        val labelVM = JLabel("Insert ViewModel name:")
        labelVM.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(labelVM)
        panel.add(Box.createVerticalStrut(4))
        stretch(viewModelField)
        panel.add(viewModelField)
        panel.add(Box.createVerticalStrut(10))

        // UseCase
        val labelUseCase = JLabel("Insert UseCase name:")
        labelUseCase.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(labelUseCase)
        panel.add(Box.createVerticalStrut(4))
        stretch(useCaseField)
        panel.add(useCaseField)
        panel.add(Box.createVerticalStrut(10))

        // Repository
        val labelRepos = JLabel("Insert Repository Name:")
        labelRepos.alignmentX = JComponent.LEFT_ALIGNMENT
        panel.add(labelRepos)
        panel.add(Box.createVerticalStrut(4))
        stretch(repositoryField)
        panel.add(repositoryField)

        return panel
    }

    fun getActivityName() = activityField.text
    fun getViewModelName() = viewModelField.text
    fun getUseCaseName() = useCaseField.text
    fun getRepositoryName() = repositoryField.text
}

class SuccessAlertDialog() : DialogWrapper(true) {
    init {
        title = "Info"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val title = "File generated successfully!\n\nCopy these to your build.gradle file:"
        val codeText = "implementation \"androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7\"\n" +
                "implementation \"androidx.lifecycle:lifecycle-livedata-ktx:2.8.7\"\n" +
                "implementation \"org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1\"\n" +
                "implementation \"com.squareup.retrofit2:retrofit:2.11.0\"\n" +
                "implementation \"com.squareup.retrofit2:converter-gson:2.11.0\""

        val textToCopy = "" +
                "implementation \"androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7\"\n" +
                "implementation \"androidx.lifecycle:lifecycle-livedata-ktx:2.8.7\"\n" +
                "implementation \"org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1\"\n" +
                "implementation \"com.squareup.retrofit2:retrofit:2.11.0\"\n" +
                "implementation \"com.squareup.retrofit2:converter-gson:2.11.0\""

        val panel = JPanel(BorderLayout())

        val textArea = JTextArea(title).apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
            background = UIManager.getColor("Label.background")
        }

        val headerPane = JScrollPane(textArea).apply {
            preferredSize = Dimension(400, 60)
            border = BorderFactory.createEmptyBorder()
        }

        val codeArea = JTextArea(codeText).apply {
            isEditable = false
            font = Font(Font.MONOSPACED, Font.PLAIN, 13)
            margin = JBUI.insets(8)
            tabSize = 2
        }

        val codePane = JBScrollPane(codeArea).apply {
            preferredSize = Dimension(520, 130)
        }

        val copyButton = JButton("Copy all")
        copyButton.addActionListener {
            val selection = StringSelection(textToCopy)
            Toolkit.getDefaultToolkit().systemClipboard.setContents(selection, null)
        }

        val bottomPanel = JPanel()
        bottomPanel.add(copyButton)

        panel.add(headerPane, BorderLayout.NORTH)
        panel.add(codePane, BorderLayout.CENTER)
        panel.add(bottomPanel, BorderLayout.SOUTH)

        return panel
    }
}