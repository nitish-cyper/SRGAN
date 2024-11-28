
# SRGAN for Image Super-Resolution on DIV2K Dataset

This repository contains a Jupyter Notebook implementation of the **Super-Resolution Generative Adversarial Network (SRGAN)**, a deep learning model designed for image super-resolution. The model is trained and evaluated on the [DIV2K dataset](https://data.vision.ee.ethz.ch/cvl/DIV2K/), which is widely used for benchmarking super-resolution algorithms.

## Features
- **SRGAN Architecture**: Combines a generator (for high-resolution image generation) and a discriminator (to distinguish real from generated images) in an adversarial setup.
- **Perceptual Loss**: Uses a combination of content loss (based on VGG features) and adversarial loss for sharper, realistic outputs.
- **DIV2K Dataset**: High-quality dataset of 2K resolution images for super-resolution tasks.
- **Training and Evaluation**: Demonstrates how to preprocess data, train SRGAN, and evaluate the results.

## Installation
1. Clone the repository and navigate to the project directory.
2. Install the required dependencies using:
   ```bash
   pip install -r requirements.txt
   ```

## Dataset
Download the DIV2K dataset from the official [website](https://data.vision.ee.ethz.ch/cvl/DIV2K/) and place it in the `data/` directory. Update the dataset path in the notebook as needed.

## Usage
1. Open the notebook file `srgan-on-div2k.ipynb` in Jupyter Notebook or JupyterLab.
2. Follow the steps in the notebook to:
   - Preprocess the DIV2K dataset
   - Define the SRGAN architecture
   - Train the SRGAN model
   - Evaluate the model on test images

## Results
The notebook provides qualitative and quantitative results demonstrating the improvement in image resolution. Example outputs include side-by-side comparisons of low-resolution inputs, SRGAN outputs, and ground truth high-resolution images.

## References
- Ledig et al., [Photo-Realistic Single Image Super-Resolution Using a Generative Adversarial Network](https://arxiv.org/abs/1609.04802)
- [DIV2K Dataset](https://data.vision.ee.ethz.ch/cvl/DIV2K/)


