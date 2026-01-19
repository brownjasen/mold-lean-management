#!/bin/bash

input=$(cat)

# Extract values using grep/sed instead of jq
cwd=$(echo "$input" | grep -o '"current_dir"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/.*"current_dir"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/')
model=$(echo "$input" | grep -o '"display_name"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/.*"display_name"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/')
used=$(echo "$input" | grep -o '"used_percentage"[[:space:]]*:[[:space:]]*[0-9]*' | sed 's/.*"used_percentage"[[:space:]]*:[[:space:]]*\([0-9]*\).*/\1/')

# Build the status line
output=""

# Add current working directory
if [ -n "$cwd" ]; then
    output="$cwd"
fi

# Add model info
if [ -n "$model" ]; then
    if [ -n "$output" ]; then
        output="$output | $model"
    else
        output="$model"
    fi
fi

# Add token usage percentage
if [ -n "$used" ]; then
    if [ -n "$output" ]; then
        output="$output | Context: ${used}% used"
    else
        output="Context: ${used}% used"
    fi
fi

# Print the final output (use printf for reliable output)
printf '%s' "$output"
