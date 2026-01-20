/*
 * Copyright 2014 Parity authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paritytrading.parity.file.taq;

import com.diffblue.cover.annotations.InterestingTestFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating TAQWriter instances for testing purposes.
 * This factory ensures proper cleanup of temporary files to satisfy
 * Diffblue Cover's requirements.
 */
public class TAQWriterFactory {

    private static final List<File> tempFiles = new ArrayList<>();
    private static final Object lock = new Object();

    /**
     * Creates a TAQWriter instance that writes to an in-memory byte array output stream.
     * This prevents the need for temporary files and allows proper flush/close operations
     * without NullPointerExceptions. This is the preferred factory method for testing
     * TAQWriter behavior without file I/O concerns.
     *
     * @return a properly initialized TAQWriter instance
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriter() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return new TAQWriter(outputStream);
    }

    /**
     * Creates a TAQWriter instance with custom configuration that writes to an in-memory
     * byte array output stream.
     *
     * @return a properly initialized TAQWriter instance with default configuration
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterWithConfig() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return new TAQWriter(outputStream, TAQConfig.DEFAULTS);
    }

    /**
     * Creates a TAQWriter instance for testing getters (getBuffer, getConfig, getPosition, getSink).
     * Uses in-memory output stream and immediately cleans up to avoid temporary file issues.
     * This is the preferred factory for testing File-based constructor and getter methods.
     *
     * @return a properly initialized TAQWriter instance
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterForGetters() {
        // Use in-memory stream to avoid file system interaction
        return new TAQWriter(new ByteArrayOutputStream());
    }

    /**
     * Creates a TAQWriter instance for testing getters with custom config.
     * Uses in-memory output stream to avoid temporary file issues.
     *
     * @return a properly initialized TAQWriter instance with default configuration
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterForGettersWithConfig() {
        return new TAQWriter(new ByteArrayOutputStream(), TAQConfig.DEFAULTS);
    }

    /**
     * Creates a TAQWriter instance for testing flush() method.
     * Uses in-memory output stream to avoid temporary file issues during flush testing.
     * This prevents the R020 issue where temporary files are not deleted.
     *
     * @return a properly initialized TAQWriter instance suitable for flush testing
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterForFlush() {
        return new TAQWriter(new ByteArrayOutputStream());
    }

    /**
     * Creates a TAQWriter instance for testing close() method.
     * Uses in-memory output stream to avoid NullPointerException and file cleanup issues.
     *
     * @return a properly initialized TAQWriter instance suitable for close testing
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterForClose() {
        return new TAQWriter(new ByteArrayOutputStream());
    }

    /**
     * Creates a TAQWriter instance that writes to a temporary file.
     * The temporary file is automatically deleted when the writer is closed or during cleanup.
     * IMPORTANT: Always use this method within a try-with-resources block to ensure proper cleanup.
     * This method satisfies Diffblue Cover's requirement that temporary files be deleted within the test.
     *
     * @return a properly initialized TAQWriter instance that writes to a temporary file
     * @throws IOException if the temporary file cannot be created
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterWithFile() throws IOException {
        File tempFile = Files.createTempFile("taq-test-", ".txt").toFile();
        registerTempFile(tempFile);
        return new TAQWriterWithCleanup(tempFile, null, tempFile);
    }

    /**
     * Creates a TAQWriter instance with custom configuration that writes to a temporary file.
     * The temporary file is automatically deleted when the writer is closed or during cleanup.
     * IMPORTANT: Always use this method within a try-with-resources block to ensure proper cleanup.
     * This method satisfies Diffblue Cover's requirement that temporary files be deleted within the test.
     *
     * @return a properly initialized TAQWriter instance with default configuration that writes to a temporary file
     * @throws IOException if the temporary file cannot be created
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterWithFileAndConfig() throws IOException {
        File tempFile = Files.createTempFile("taq-test-", ".txt").toFile();
        registerTempFile(tempFile);
        return new TAQWriterWithCleanup(tempFile, TAQConfig.DEFAULTS, tempFile);
    }

    /**
     * Creates a TAQWriter instance using a File constructor.
     * The temporary file is automatically deleted when the writer is closed or during cleanup.
     * This method is specifically for testing the File-based constructor.
     * IMPORTANT: Always use this method within a try-with-resources block to ensure proper cleanup.
     *
     * @return a properly initialized TAQWriter instance created via File constructor
     * @throws IOException if the temporary file cannot be created
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterViaFileConstructor() throws IOException {
        File tempFile = Files.createTempFile("taq-test-", ".txt").toFile();
        registerTempFile(tempFile);
        return new TAQWriterWithCleanup(tempFile, null, tempFile);
    }

    /**
     * Creates a TAQWriter instance using a File constructor with custom configuration.
     * The temporary file is automatically deleted when the writer is closed or during cleanup.
     * This method is specifically for testing the File-based constructor with config.
     * IMPORTANT: Always use this method within a try-with-resources block to ensure proper cleanup.
     *
     * @param config the TAQ configuration to use
     * @return a properly initialized TAQWriter instance created via File constructor with config
     * @throws IOException if the temporary file cannot be created
     */
    @InterestingTestFactory
    public static TAQWriter createTAQWriterViaFileConstructor(TAQConfig config) throws IOException {
        File tempFile = Files.createTempFile("taq-test-", ".txt").toFile();
        registerTempFile(tempFile);
        return new TAQWriterWithCleanup(tempFile, config, tempFile);
    }

    /**
     * Registers a temporary file for cleanup. This file will be deleted on JVM exit
     * if not already deleted.
     *
     * @param file the file to register
     */
    private static void registerTempFile(File file) {
        synchronized (lock) {
            tempFiles.add(file);
        }
        file.deleteOnExit();
    }

    /**
     * Cleans up all temporary files created by this factory.
     * This method should be called in test teardown (@AfterEach) if needed.
     */
    public static void cleanupTempFiles() {
        synchronized (lock) {
            for (File file : tempFiles) {
                if (file.exists()) {
                    file.delete();
                }
            }
            tempFiles.clear();
        }
    }

    /**
     * A TAQWriter wrapper that automatically deletes the temporary file when closed.
     * This ensures that temporary files created during testing are properly cleaned up,
     * satisfying Diffblue Cover's requirement that temporary files be deleted within
     * the method under test.
     */
    private static class TAQWriterWithCleanup extends TAQWriter {
        private final File fileToDelete;

        TAQWriterWithCleanup(File file, TAQConfig config, File fileToDelete) throws FileNotFoundException {
            super(file, config == null ? TAQConfig.DEFAULTS : config);
            this.fileToDelete = fileToDelete;
        }

        @Override
        public void close() {
            super.close();
            deleteFile();
        }

        @Override
        public void flush() {
            super.flush();
            // Note: We don't delete the file on flush, only on close
            // This allows multiple flush operations before the final close
        }

        private void deleteFile() {
            if (fileToDelete != null && fileToDelete.exists()) {
                fileToDelete.delete();
                synchronized (lock) {
                    tempFiles.remove(fileToDelete);
                }
            }
        }
    }
}
