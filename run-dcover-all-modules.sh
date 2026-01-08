#!/bin/bash

# Script to run dcover create --agent=claude in each module directory
# of the Parity trading platform

set -e

# Define the modules from the parent pom.xml
modules=(
    "libraries/book"
    "libraries/file"
    "libraries/match"
    "libraries/net"
    "libraries/util"
    "applications/client"
    "applications/fix"
    "applications/reporter"
    "applications/system"
    "applications/ticker"
)

# Get the script's directory (project root)
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Running dcover create --agent=claude in all modules..."
echo "=========================================="

# Track success and failures
success_count=0
failure_count=0
failed_modules=()

# Iterate through each module
for module in "${modules[@]}"; do
    echo ""
    echo "Processing module: $module"
    echo "----------------------------------------"

    module_path="$PROJECT_ROOT/$module"

    if [ ! -d "$module_path" ]; then
        echo "WARNING: Module directory not found: $module_path"
        ((++failure_count))
        failed_modules+=("$module (directory not found)")
        continue
    fi

    # Change to module directory and run dcover
    cd "$module_path"

    if /home/chubert/myProjects/cover/cover-cli/target/dcover create --agent=claude; then
        echo "SUCCESS: Completed for $module"
        ((++success_count))
    else
        echo "ERROR: Failed for $module"
        ((++failure_count))
        failed_modules+=("$module")
    fi

    # Return to project root
    cd "$PROJECT_ROOT"
done

# Summary
echo ""
echo "=========================================="
echo "Summary:"
echo "  Successful: $success_count"
echo "  Failed: $failure_count"

if [ ${#failed_modules[@]} -gt 0 ]; then
    echo ""
    echo "Failed modules:"
    for failed in "${failed_modules[@]}"; do
        echo "  - $failed"
    done
fi

echo "=========================================="

# Exit with error code if any modules failed
if [ $failure_count -gt 0 ]; then
    exit 1
fi
